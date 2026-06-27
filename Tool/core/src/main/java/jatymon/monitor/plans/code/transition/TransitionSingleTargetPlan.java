package jatymon.monitor.plans.code.transition;

import com.palantir.javapoet.CodeBlock;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.monitor.plans.types.StatesEnumPlan;
import jatymon.typestate.graph.transitions.GraphSingleTransition;

/**
 * Class {@code TransitionSingleTargetPlan} represents the code structure for the actual transition mechanism
 *  (changing the value of the current state) for a {@code GraphSingleTransition}. This means it handles only one output possibility
 * @author Francisco Parrinha
 */
public class TransitionSingleTargetPlan extends TransitionTargetPlan {
    public TransitionSingleTargetPlan(final GraphSingleTransition transition) {
        super(transition);
    }

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        final String outputName = ((GraphSingleTransition) graphTransition).getOut().getName();
        final CodeBlock.Builder code = CodeBlock.builder()
                .addStatement(CurrentStateFieldPlan.getAssignCode(ctx.typestateData().getKey(),
                        CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), outputName)));

        // Add post-assignments
        graphTransition.getPostAssignments().forEach(a -> code.addStatement("$L()", a));
        return code.build();
    }
}
