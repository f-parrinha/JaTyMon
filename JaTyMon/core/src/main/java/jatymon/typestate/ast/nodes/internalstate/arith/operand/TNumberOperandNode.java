package jatymon.typestate.ast.nodes.internalstate.arith.operand;

import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TNumberOperandNode extends TOperandNode<Integer> {
    public static final String TYPE = "numberOperand";

    public TNumberOperandNode(final TokenPosition tokenPos, final Integer number) {
        super(tokenPos, number);
    }

    @Override
    public String toCode() {
        return getValue().toString();
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
        return obj instanceof TNumberOperandNode other &&
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
