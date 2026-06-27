package jatymon.typestate.ast.nodes.internalstate.arith.operand;

import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.UndefinedFieldDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TIdOperandNode extends TOperandNode<String> {
    public static final String TYPE = "idOperand";

    public TIdOperandNode(final TokenPosition tokenPos, final String value) {
        super(tokenPos, value);
    }

    @Override
    public String toCode() {
        return getValue();
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final String id = getValue();
        if (!ctx.containsExt(id) && !ctx.containsVal(id)) {
            diagnostics.add(new UndefinedFieldDiagnostic(id, ctx.getTypestateName(), getTokenPosition()));
        }
        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TIdOperandNode other &&
                super.equals(other);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.VALUE, getValue());
        return json;
    }
}
