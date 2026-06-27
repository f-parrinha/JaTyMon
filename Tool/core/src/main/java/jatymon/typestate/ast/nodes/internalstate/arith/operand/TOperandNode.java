package jatymon.typestate.ast.nodes.internalstate.arith.operand;

import jatymon.typestate.ast.nodes.internalstate.arith.TArithExprNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.internalstate.predicates.TBinaryBoolExprNode;

import java.util.Map;

public abstract class TOperandNode<T> extends TArithExprNode {
    private final T value;
    public TOperandNode(final TokenPosition tokenPos, final T value) {
        super(tokenPos);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public boolean isNumber() {
        return this instanceof TNumberOperandNode;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TOperandNode<?> other &&
                super.equals(other) &&
                other.value.equals(value);
    }
}
