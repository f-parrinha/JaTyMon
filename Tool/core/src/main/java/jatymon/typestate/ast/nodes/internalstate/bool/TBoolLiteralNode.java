package jatymon.typestate.ast.nodes.internalstate.bool;

import jatymon.common.TokenPosition;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import java.util.List;
import java.util.Map;

public class TBoolLiteralNode extends TBoolExprNode {
    public static final String TYPE = "boolLiteral";

    private final boolean value;

    public TBoolLiteralNode(final TokenPosition tokenPos, final boolean value) {
        super(tokenPos);
        this.value = value;
    }

    public boolean getValue() {
        return value;
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
        return Boolean.toString(value);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TBoolLiteralNode other &&
                super.equals(other) &&
                other.value == value;
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.VALUE, value);
        return json;
    }
}
