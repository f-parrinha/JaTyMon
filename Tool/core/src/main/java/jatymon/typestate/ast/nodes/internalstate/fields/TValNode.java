package jatymon.typestate.ast.nodes.internalstate.fields;

import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TIdOperandNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TNumberOperandNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TOperandNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.DuplicateFieldDiagnostic;
import jatymon.diagnostics.semantic.internalstate.UndefinedFieldDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TValNode extends TFieldNode {
    public final String TYPE = "val";


    private final TOperandNode<?> operand;

    public TValNode(final TokenPosition tokenPos, final String name, final TOperandNode<?> operand) {
        super(tokenPos, name);
        this.operand = operand;
    }

    public static TValNode createDefault(final TokenPosition tokenPos, final String name) {
        return new TValNode(tokenPos, name, new TNumberOperandNode(tokenPos, 0));
    }

    public TOperandNode<?> getOperand() {
        return operand;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(operand);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final String name = getName();
        final String protocolName = ctx.getTypestateName();
        final TokenPosition tokenPos = getTokenPosition();

        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        if (!ctx.addVal(this) || ctx.containsExt(name)) {
            diagnostics.add(new DuplicateFieldDiagnostic(name, ctx.getTypestateName(), tokenPos));
        }
        if (operand instanceof TIdOperandNode idOperand && !ctx.containsField(idOperand.getValue())) {
            diagnostics.add(new UndefinedFieldDiagnostic(idOperand.getValue(), protocolName, tokenPos));
        }

        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TValNode other &&
                super.equals(other) &&
                other.operand.equals(operand);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, getName());
        json.put(JsonKeys.OPERAND, operand.toJson());
        return json;
    }
}
