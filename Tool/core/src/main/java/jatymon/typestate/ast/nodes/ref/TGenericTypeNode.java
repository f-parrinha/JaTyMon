package jatymon.typestate.ast.nodes.ref;

import jatymon.common.TokenPosition;
import jatymon.common.globals.JsonKeys;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TGenericTypeNode extends TRefNode {
    public static final String TYPE = "genericType";

    private final TRefNode ref;
    private final List<TRefNode> typeArgs;

    public TGenericTypeNode(final TokenPosition tokenPos, final TRefNode ref, final List<TRefNode> typeArgs) {
        super(tokenPos);

        this.ref = ref;
        this.typeArgs = typeArgs;
    }

    public TRefNode getRef() {
        return ref;
    }

    @Override
    public String getFullName() {
        return String.format("%s<%s>", ref.getFullName(), typeArgs.stream()
                .map(TRefNode::getFullName)
                .collect(Collectors.joining(",")));
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toString() {
        return String.format("[TGenericTypeNode {ref: %s, typeArgs: %s}]", ref, typeArgs);
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof TGenericTypeNode other &&
                super.equals(other) &&
                other.ref.equals(ref) &&
                other.typeArgs.equals(typeArgs);
    }

    @Override
    public List<TNode> getChildren() {
        final List<TNode> children = new LinkedList<>();
        children.add(ref);
        children.addAll(typeArgs);
        return children;
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.REF, ref.toJson());
        json.put(JsonKeys.TYPE_ARGS, typeArgs.stream()
                .map(TRefNode::toJson)
                .toList());
        json.put(JsonKeys.STRING, getFullName());
        return  json;
    }
}
