package jatymon.typestate.ast.nodes.ref;

import jatymon.common.*;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;
import java.util.Map;

public class TIdNode extends TRefNode implements Nameable {
    public static final String TYPE = "id";

    private final String name;

    public TIdNode(final TokenPosition tokenPos, final String name) {
        super(tokenPos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toString() {
        return String.format("[TIdNode {name: %s}]", name);
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof TIdNode other &&
                super.equals(other) &&
                other.name.equals(name);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        json.put(JsonKeys.STRING, getFullName());
        return json;
    }
}
