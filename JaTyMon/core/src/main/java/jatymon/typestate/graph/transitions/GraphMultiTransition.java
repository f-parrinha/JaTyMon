package jatymon.typestate.graph.transitions;

import jatymon.actions.ActionType;
import jatymon.ratios.Ratio;
import jatymon.typestate.graph.nodes.GraphNode;

import java.util.*;


/**
 * Class {@code GraphMultiTransition} represents a transition in the graph containing several output graph nodes, each
 *  mapped by decision.
 * @author Francisco Parrinha
 */
public class GraphMultiTransition extends GraphTransition {

    /**
     * Decisions are enumerable values returned by the methods.
     * @param label decision name
     * @param destination output graph node
     * @author Francisco Parrinha
     */
    public record Decision(String label, GraphNode destination) { /* Nothing to do here... */ }

    private final Map<String, GraphNode> outs;

    public GraphMultiTransition(final String name,
                                final GraphNode in,
                                final Set<Decision> outs,
                                final ActionType actionType) {
        super(name, in, actionType);
        this.outs = new TreeMap<>();

        // Load decisions
        for (final Decision out : outs) {
            this.outs.put(out.label, out.destination);
        }
    }

    /**
     * Returns the map of decisions to output graph nodes, this is, to where the transition leads to depending on the value of a decision.
     * @return output graph node
     */
    public Map<String, GraphNode> getOuts() {
        return Map.copyOf(outs);
    }

    @Override
    public String id() {
        return String.format("%s:%s:%s", in, name, outs);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof GraphMultiTransition other && other.outs.keySet().equals(outs.keySet());
    }

    public static final class Builder implements GraphTransitionBuilder {
        private final GraphMultiTransition instance;

        public Builder(final String name, final GraphNode in, final Set<Decision> outs, final ActionType actionType) {
            instance = new GraphMultiTransition(name, in, outs, actionType);
        }

        @Override
        public Builder withRatio(final Ratio ratio) {
            instance.ratio = ratio;
            return this;
        }

        @Override
        public Builder withPreAssignments(final Set<String> preAssignments) {
            instance.preAssignments = preAssignments;
            return this;
        }

        @Override
        public Builder withPostAssignments(final Set<String> postAssignments) {
            instance.postAssignments = postAssignments;
            return this;
        }

        @Override
        public Builder withPredicates(final Set<String> predicates) {
            instance.predicates = predicates;
            return this;
        }

        @Override
        public Builder withArgs(List<String> argsTypes) {
            instance.args = argsTypes;
            return this;
        }

        @Override
        public Builder withReturnType(String returnType) {
            instance.returnType = returnType;
            return this;
        }

        public GraphMultiTransition build() {
            return instance;
        }
    }
}
