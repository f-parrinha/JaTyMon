package jatymon.monitor.plans.code.transition;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.code.superCall.SuperCallPlan;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.typestate.graph.transitions.GraphTransition;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Class {@code TransitionCasePlan} represents the structure of a single case statement in the transition's overall switch-case
 *  statement, controlling and dispatching the correct code/transitions for each transition case.
 * @param inputState input state name
 * @param transitionName transition name
 * @param preAssignments set of pre-assignment names
 * @param predicates set of predicate-names
 * @param postAssignments set of post-assignment names
 * @param ratio assigned true/expected ratio
 * @param targetPlan target code plan
 * @author Francisco Parrinha
 */
public record TransitionCasePlan(String inputState,
                                 String transitionName,
                                 ActionType actionType,
                                 Set<String> preAssignments,
                                 Set<String> predicates,
                                 Set<String> postAssignments,
                                 Ratio ratio,
                                 TransitionTargetPlan targetPlan) implements CodePlan {
    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        return emit(ctx, null);
    }

    /**
     * Creates the code block for a case statement representing a particular transition {@code (in -> t -> out)}. Note each transition
     *  contains a unique method for its unique signature, however, since they may be called in different states, a switch-case
     *  is used to determine which particular transition is being triggered according to the current state.
     * @param ctx monitor builder context
     * @return JavaPoet {@code CodeBlock}
     */
    public CodeBlock emit(final MonitorFactory.BuildContext ctx, final SuperCallPlan superCallPlan) {
        final CodeBlock.Builder code = CodeBlock.builder();

        // Make transitions with ratio monitorable
        if (!(ratio instanceof NullRatio)) {
            code.addStatement("validateRatio(new $T($S, $S, $T.$L), $L)", ClassName.get(ActionId.class),
                    inputState, transitionName, ClassName.get(ActionType.class), actionType, ratio);
        }

        // Call pre-assignments
        preAssignments.forEach(a -> code.addStatement("$L()", a));
        boolean isSuperCallEarly = targetPlan instanceof TransitionMultiTargetPlan || ctx.typestateData().getKey() != null;

        // Add super call in before transition to get output destination from the decision state
        if (isSuperCallEarly && superCallPlan != null) {
            code.add(superCallPlan.emit(ctx));
        }

        // Add guarded (by predicates) transition
        final CodeBlock targetCode = targetPlan.emit(ctx);
        if (predicates.isEmpty()) {
            code.add(targetCode);
        } else {
            code.beginControlFlow("if ($L)", predicates.stream()
                        .map(p -> p + "()")
                        .collect(Collectors.joining(" || ")))
                    .add(targetCode)
                    .endControlFlow();
        }

        // Add super call at last if the transition is simple
        if (!isSuperCallEarly && superCallPlan != null) {
            code.add(superCallPlan.emit(ctx));
        }
        return code.build();
    }

    /**
     * Creates a {@code TransitionCasePlan} instance given a {@code GraphTransition}
     * @param t {@code GraphTransition} instance
     * @return new {@code TransitionCasePlan} instance
     */
    public static TransitionCasePlan fromGraphTransition(final GraphTransition t) {
        return new TransitionCasePlan(
                t.getIn().getName(),
                t.getName(),
                t.getActionType(),
                t.getPreAssignments(),
                t.getPredicates(),
                t.getPostAssignments(),
                t.getRatio(),
                TransitionTargetPlan.fromGraphTransition(t));
    }
}

