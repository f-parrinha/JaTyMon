package jatymon.typestate.ast.nodes.internalstate.predicates;

import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.internalstate.Expression;

public abstract class TBoolExprNode extends TNode implements Expression {
    public TBoolExprNode(final TokenPosition tokenPos) {
        super(tokenPos);
    }
}
