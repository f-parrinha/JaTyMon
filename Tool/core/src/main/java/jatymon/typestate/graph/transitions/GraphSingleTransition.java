package jatymon.typestate.graph.transitions;

import jatymon.actions.ActionType;
import jatymon.ratios.Ratio;
import jatymon.typestate.graph.nodes.GraphNode;

import java.util.List;
import java.util.Set;

/**
 * Class {@code GraphSingleTransition} represents a simple transition in the graph having only a single output graph node.
 */
public class GraphSingleTransition extends GraphTransition {
    private final GraphNode out;

    public GraphSingleTransition(final String name,
                                 final GraphNode in,
                                 final GraphNode out,
                                 final ActionType actionType) {
        super(name, in, actionType);
        this.out = out;
    }

    /**
     * Returns the output graph node, this, to where the transition leads to.
     * @return output graph node
     */
    public GraphNode getOut() {
        return out;
    }

    public String id() {
        return String.format("%s:%s:%s", in, name, out);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof GraphSingleTransition other && other.out.getName().equals(out.getName());
    }

    public static final class Builder implements GraphTransitionBuilder {
        private final GraphSingleTransition instance;

        public Builder(final String name, final GraphNode in, final GraphNode out, final ActionType actionType) {
            instance = new GraphSingleTransition(name, in, out, actionType);
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

        public GraphSingleTransition build() {
            return instance;
        }
    }
}
