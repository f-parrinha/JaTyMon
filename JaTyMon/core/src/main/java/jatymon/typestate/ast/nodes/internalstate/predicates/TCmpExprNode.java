package jatymon.typestate.ast.nodes.internalstate.predicates;

import jatymon.common.globals.ComparisonOperator;
import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.arith.TArithExprNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TCmpExprNode extends TBoolExprNode {
    public static final String TYPE = "cmpExpr";

    private final TArithExprNode arith1;
    private final TArithExprNode arith2;
    private final ComparisonOperator operator;

    public TCmpExprNode(final TokenPosition tokenPos, final TArithExprNode arith1, final TArithExprNode arith2, final ComparisonOperator operator) {
        super(tokenPos);
        this.arith1 = arith1;
        this.arith2 = arith2;
        this.operator = operator;
    }

    public TArithExprNode getArith1() {
        return arith1;
    }

    public TArithExprNode getArith2() {
        return arith2;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    @Override
    public String toCode() {
        return "%s %s %s".formatted(arith1.toCode(), ComparisonOperator.toCode(operator), arith2.toCode());
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TCmpExprNode other &&
                super.equals(other) &&
                other.arith1.equals(arith1) &&
                other.arith2.equals(arith2);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.ARITH_1, arith1.toJson());
        json.put(JsonKeys.ARITH_2, arith2.toJson());
        return json;
    }
}
