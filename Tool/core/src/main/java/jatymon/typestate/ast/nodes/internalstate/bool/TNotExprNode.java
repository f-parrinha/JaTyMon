package jatymon.typestate.ast.nodes.internalstate.bool;

import jatymon.common.TokenPosition;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import java.util.List;
import java.util.Map;

public class TNotExprNode extends TBoolExprNode {
    public static final String TYPE = "notExpr";

    private final TBoolExprNode boolExpr;

    public TNotExprNode(final TokenPosition tokenPos, final TBoolExprNode boolExpr) {
        super(tokenPos);
        this.boolExpr = boolExpr;
    }

    public TBoolExprNode getBoolExpr() { return boolExpr; }

    @Override
    public List<TNode> getChildren() {
        return List.of(boolExpr);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(Resolver resolver, TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toCode() {
        return "!%s".formatted(boolExpr.toCode());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TNotExprNode other &&
                super.equals(other) &&
                other.boolExpr.equals(this.boolExpr);
    }

    @Override public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.BOOL_EXPR, boolExpr.toJson());
        return json;
    }
}
