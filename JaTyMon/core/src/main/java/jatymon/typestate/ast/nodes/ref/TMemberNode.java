package jatymon.typestate.ast.nodes.ref;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TMemberNode extends TRefNode {
    public static final String TYPE = "member";

    private final TRefNode ref;
    private final TIdNode id;

    public TMemberNode(final TokenPosition tokenPos, final TRefNode ref, final TIdNode id) {
        super(tokenPos);
        this.ref = ref;
        this.id = id;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toString() {
        return String.format("[TMemberNode {ref: %s, id: %s}]", ref, id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TMemberNode other &&
                super.equals(other) &&
                other.ref.equals(ref) &&
                other.id.equals(id);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(ref, id);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.REF, ref.toJson());
        json.put(JsonKeys.ID, id.toJson());
        json.put(JsonKeys.STRING, getFullName());
        return json;
    }

    @Override
    public String getFullName() {
        return String.format("%s.%s", ref.getFullName(), id.getFullName());
    }
}
