package jatymon.typestate.ast.nodes.ref;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TArrayTypeNode extends TRefNode {
    public static final String TYPE = "arrayType";

    private final TRefNode ref;

    public TArrayTypeNode(final TokenPosition tokenPos, final TRefNode ref) {
        super(tokenPos);

        this.ref = ref;
    }

    public TRefNode getRef() {
        return ref;
    }

    @Override
    public String getFullName() {
        return String.format("%s[]", ref.getFullName());
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toString() {
        return String.format("[TArrayTypeNode {ref: %s[]}]", ref);
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof TArrayTypeNode other &&
                super.equals(other) &&
                other.ref.equals(ref);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(ref);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.REF, ref.toJson());
        json.put(JsonKeys.STRING, getFullName());
        return  json;
    }
}
