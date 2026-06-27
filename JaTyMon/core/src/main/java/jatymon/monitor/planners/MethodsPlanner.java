package jatymon.monitor.planners;

import jatymon.common.ActionSignature;
import jatymon.resolving.Resolver;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.methods.constructors.ParameterizedConstructorPlan;
import jatymon.monitor.plans.methods.constructors.SimpleConstructorPlan;
import jatymon.monitor.plans.fields.FieldPlan;
import jatymon.monitor.plans.methods.*;
import jatymon.monitor.plans.code.transition.TransitionCasePlan;
import jatymon.typestate.TypestateData;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.TPredNode;
import jatymon.typestate.graph.GraphUtils;
import jatymon.typestate.graph.transitions.GraphTransition;

import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class {code MethodsPlanner} contains methods to create the different methods that a monitor may use. Using LinkedHashSets
 *  to guarantee uniqueness in the created methods while maintaining order.
 * @author Francisco Parrinha
 */
public class MethodsPlanner implements Planner<MethodPlan> {
    private static final MethodsPlanner INSTANCE = new MethodsPlanner();

    public static MethodsPlanner getInstance() {
        return INSTANCE;
    }


    @Override
    public Set<MethodPlan> plan(final MonitorFactory.BuildContext ctx) {
        final TypestateData typestateData = ctx.typestateData();
        Set<MethodPlan> res =  new LinkedHashSet<>();
        res.addAll(planTransitions(ctx));
        res.addAll(planAssignments(typestateData.getAssignments()));
        res.addAll(planPredicates(typestateData.getPredicates()));
        res.add(new ValidateRatioMethodPlan());
        return res;
    }

    /**
     * Creates a set with all required methods, including the constructor with correct field initialization expressions
     * @param ctx monitor building context instance
     * @param fieldsToInit set of fields to be initialized in the constructor
     * @return set with method plans
     */
    public Set<MethodPlan> plan(final MonitorFactory.BuildContext ctx, final Set<FieldPlan> fieldsToInit) {
        final Set<MethodPlan> res = plan(ctx);
        res.add(new SimpleConstructorPlan(fieldsToInit));
        res.add(new ParameterizedConstructorPlan(fieldsToInit));
        return res;
    }


    /**
     * Creates a set of method plans for transition methods in the monitor
     * @param ctx monitor building context instance
     * @return set with transition plans
     */
    public Set<TransitionMethodPlan> planTransitions(final MonitorFactory.BuildContext ctx) {
        final TypestateData typestateData = ctx.typestateData();
        final Resolver resolver = ctx.resolver();

        // TODO: stop using getTransitionsBySignature and go through the graph, store computed stuff in a map, return the result as a set

        final Map<ActionSignature, Set<GraphTransition>> transitionsByName = GraphUtils.getTransitionsBySignature(typestateData.getGraph());
        final Set<TransitionMethodPlan> res = new LinkedHashSet<>();
        for (final Map.Entry<ActionSignature, Set<GraphTransition>> entry : transitionsByName.entrySet()) {
            final Set<GraphTransition> transitions = entry.getValue();

            // Note: All transitions in the set are the same. They simply come from different states.
            final GraphTransition transition = transitions.iterator().next();
            final String name = transition.getName();
            final TransitionTypes transitionTypes = getTransitionTypes(transition, resolver, typestateData);
            res.add(new TransitionMethodPlan(name,
                    transitionTypes.returnType,
                    transitionTypes.argsTypes,
                    transitionTypes.thrownTypes,
                    transition.getActionType(),
                    transitions.stream()
                            .map(TransitionCasePlan::fromGraphTransition)
                            .collect(Collectors.toSet())));
        }
        return res;
    }

    /**
     * Creates a set of method plans for each assignment in the typestate
     * @param assignNodes set of AST assignment nodes
     * @return set with assignment method plans
     */
    public Set<AssignmentMethodPlan> planAssignments(final Set<TAssignNode> assignNodes) {
        final Set<AssignmentMethodPlan> res = new LinkedHashSet<>();
        assignNodes.forEach(a -> res.add(AssignmentMethodPlan.fromTAssignNode(a)));
        return res;
    }

    /**
     * Creates a set of method plans for each predicate in the typestate
     * @param predNodes set of AST predicate nodes
     * @return set with predicate method plans
     */
    public Set<PredicateMethodPlan> planPredicates(final Set<TPredNode> predNodes) {
        final Set<PredicateMethodPlan> res = new LinkedHashSet<>();
        predNodes.forEach(p -> res.add(PredicateMethodPlan.fromTPredNode(p)));
        return res;
    }


    private TransitionTypes getTransitionTypes(final GraphTransition transition,
                                                    final Resolver resolver,
                                                    final TypestateData typestateData) {
        final String typestateQfName = typestateData.getQualifiedName();
        final String packageName = typestateData.getPackageName();
        final Set<String> imports = typestateData.getImports();

        // Collect types (return, args and thrown)
        final String returnTypeQfName = resolver.qualifyName(transition.getReturnType(), imports, packageName, typestateQfName);
        final TypeMirror returnType = resolver.resolveTypeMirror(returnTypeQfName);
        final List<String> argsQfNames = transition.getArgs().stream()
                .map(arg -> resolver.qualifyName(arg, imports, packageName, typestateQfName))
                .toList();
        final List<TypeMirror> argsTypes = argsQfNames.stream()
                .map(resolver::resolveTypeMirror)
                .toList();
        final List<? extends TypeMirror> thrownTypes = resolver.getThrownTypes(typestateData.getClassQfName(), transition.getName(), returnTypeQfName, argsQfNames);
        return new TransitionTypes(returnType, argsTypes, thrownTypes);
    }

    private record TransitionTypes(TypeMirror returnType, List<TypeMirror> argsTypes, List<? extends TypeMirror> thrownTypes) { }
}
