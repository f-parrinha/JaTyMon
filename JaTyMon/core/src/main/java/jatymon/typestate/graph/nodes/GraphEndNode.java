package jatymon.typestate.graph.nodes;

import jatymon.typestate.ast.nodes.state.TStateNode;

public class GraphEndNode extends GraphNode {
    public GraphEndNode() {
        super(TStateNode.END_STATE);
    }
}
