package jatymon.typestate.ast.nodes.internalstate.arith;

import jatymon.common.globals.ArithOperator;
import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TBinaryArithExprNode extends TArithExprNode {
    public static final String TYPE = "binaryArithExpr";

    private final TArithExprNode arith1;
    private final TArithExprNode arith2;
    private final ArithOperator arithOperator;

    public TBinaryArithExprNode(final TokenPosition tokenPos,
                                final TArithExprNode arith1,
                                final TArithExprNode arith2,
                                final ArithOperator arithOperator) {
        super(tokenPos);
        this.arith1 = arith1;
        this.arith2 = arith2;
        this.arithOperator = arithOperator;
    }

    public TArithExprNode getArith1() {
        return arith1;
    }

    public TArithExprNode getArith2() {
        return arith2;
    }

    public ArithOperator getOperator() {
        return arithOperator;
    }

    @Override
    public String toCode() {
        return "%s %s %s".formatted(arith1.toCode(), ArithOperator.toString(arithOperator), arith2.toCode());
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(this.arith1, this.arith2);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TBinaryArithExprNode other &&
                super.equals(other) &&
                other.arith1.equals(arith2) &&
                other.arith2.equals(arith2) &&
                other.arithOperator.equals(arithOperator);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.ARITH_1, arith1.toJson());
        json.put(JsonKeys.OPERATOR, arithOperator);
        json.put(JsonKeys.ARITH_2, arith2.toJson());
        return json;
    }

}
