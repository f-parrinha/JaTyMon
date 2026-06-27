package jatymon.typestate.ast.nodes.internalstate.assignments;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.arith.TArithExprNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.ExtFieldsAreImmutableDiagnostic;
import jatymon.diagnostics.semantic.internalstate.UndefinedFieldDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TAssignExprNode extends TNode {
    public static final String TYPE = "assignExpr";
    private final String valName;
    private final TArithExprNode arithExpr;

    public TAssignExprNode(final TokenPosition tokenPos, final String valName, final TArithExprNode arithExpr) {
        super(tokenPos);
        this.valName = valName;
        this.arithExpr = arithExpr;
    }

    public TArithExprNode getArithExpr() {
        return arithExpr;
    }

    public String getValName() {
        return valName;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(arithExpr);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final String protoName = ctx.getTypestateName();
        final TokenPosition tokenPos = getTokenPosition();
        if (ctx.containsExt(valName)) {
            diagnostics.add(new ExtFieldsAreImmutableDiagnostic(protoName, tokenPos));
        } else if (!ctx.containsVal(valName)) {
            diagnostics.add(new UndefinedFieldDiagnostic(valName, protoName, tokenPos));
        }
        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TAssignExprNode other &&
                super.equals(other) &&
                other.valName.equals(valName) &&
                other.arithExpr.equals(arithExpr);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.VAL_NAME, valName);
        json.put(JsonKeys.ARITH_EXPR, arithExpr.toJson());
        return json;
    }
}
