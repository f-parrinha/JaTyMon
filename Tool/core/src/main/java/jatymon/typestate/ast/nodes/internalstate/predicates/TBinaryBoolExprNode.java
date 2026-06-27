package jatymon.typestate.ast.nodes.internalstate.predicates;

import jatymon.resolving.Resolver;
import jatymon.common.globals.BoolOperator;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TBinaryBoolExprNode extends TBoolExprNode {
    private final TBoolExprNode boolExpr1;
    private final TBoolExprNode boolExpr2;
    private final BoolOperator boolOperator;

    public TBinaryBoolExprNode(final TokenPosition tokenPos,
                               final TBoolExprNode boolExpr1,
                               final TBoolExprNode boolExpr2,
                               final BoolOperator boolOperator) {
        super(tokenPos);
        this.boolExpr1 = boolExpr1;
        this.boolExpr2 = boolExpr2;
        this.boolOperator = boolOperator;
    }

    public TBoolExprNode getBoolExpr1() {
        return boolExpr1;
    }

    public TBoolExprNode getBoolExpr2() {
        return boolExpr2;
    }

    public BoolOperator getOperator() {
        return boolOperator;
    }

    @Override
    public String toCode() {
        return "%s %s %s".formatted(boolExpr1.toCode(), BoolOperator.toString(boolOperator), boolExpr2.toCode());
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
        return obj instanceof TBinaryBoolExprNode other &&
                super.equals(other) &&
                other.boolExpr1.equals(boolExpr1) &&
                other.boolExpr2.equals(boolExpr2) &&
                other.boolOperator.equals(boolOperator);
    }

    @Override
    public Map<String, Object> toJson() {
        var json =  super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.BOOL_EXPR_1, boolExpr1.toJson());
        json.put(JsonKeys.OPERATOR, boolOperator);
        json.put(JsonKeys.BOOL_EXPR_2, boolExpr2.toJson());
        return json;
    }

}
