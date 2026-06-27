package jatymon.typestate.ast.nodes.ref;

import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;

public abstract class TRefNode extends TNode {
    public TRefNode(final TokenPosition tokenPos) {
        super(tokenPos);
    }

    public abstract String getFullName();
}
