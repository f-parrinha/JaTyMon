package jatymon.typestate.ast.nodes.internalstate.fields;

import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.TBinaryBoolExprNode;

public abstract class TFieldNode extends TNode {
    private final String name;

    public TFieldNode(final TokenPosition tokenPos, final String name) {
        super(tokenPos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TFieldNode other &&
                super.equals(other) &&
                other.name.equals(name);
    }
}
