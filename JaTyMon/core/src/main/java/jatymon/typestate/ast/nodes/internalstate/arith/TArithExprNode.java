package jatymon.typestate.ast.nodes.internalstate.arith;

import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.internalstate.Expression;

public abstract class TArithExprNode extends TNode implements Expression {
    public TArithExprNode(final TokenPosition tokenPos) {
        super(tokenPos);
    }
}
