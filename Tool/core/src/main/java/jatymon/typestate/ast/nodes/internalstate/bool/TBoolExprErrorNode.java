package jatymon.typestate.ast.nodes.internalstate.bool;

import jatymon.common.TokenPosition;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import java.util.List;
import java.util.Map;

public class TBoolExprErrorNode extends TBoolExprNode {
    public static final String TYPE = "boolExprError";

    public TBoolExprErrorNode(final TokenPosition tokenPos) {
        super(tokenPos);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver, final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toCode() {
        return "";
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TBoolExprErrorNode other &&
                super.equals(other);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        return json;
    }
}
