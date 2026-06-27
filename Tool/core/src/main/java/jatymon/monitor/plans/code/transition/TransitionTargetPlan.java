package jatymon.monitor.plans.code.transition;

import jatymon.monitor.plans.code.CodePlan;
import jatymon.exceptions.graph.UnknownTransitionTypeException;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;

/**
 * Abstract class {@code TransitionTargetPlan} represents the code structure for the actual transition mechanism
 *  (changing the value of the current state).
 * @author Francisco Parrinha
 */
public abstract class TransitionTargetPlan implements CodePlan {
    protected final GraphTransition graphTransition;

    public TransitionTargetPlan(final GraphTransition transition) {
        this.graphTransition = transition;
    }

    /**
     * Creates a {@code TransitionTargetPlan} instance given a {@code GraphTransition}
     * @param transition {@code GraphTransition} instance
     * @return new {@code TransitionTargetPlan} instance
     */
    public static TransitionTargetPlan fromGraphTransition(final GraphTransition transition) {
        return switch (transition) {
            case GraphSingleTransition s -> new TransitionSingleTargetPlan(s);
            case GraphMultiTransition m -> new TransitionMultiTargetPlan(m);
            default -> throw new UnknownTransitionTypeException(transition.getClass());
        };
    }
}
