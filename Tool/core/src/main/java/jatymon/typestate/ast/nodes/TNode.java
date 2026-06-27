package jatymon.typestate.ast.nodes;

import jatymon.common.globals.JsonKeys;
import jatymon.common.JsonSerializable;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.*;

public abstract class TNode implements JsonSerializable {
    public static final String TYPE = "(generic_node)";

    private final TokenPosition tokenPos;

    public TNode(final TokenPosition tokenPos) {
        this.tokenPos = tokenPos;
    }

    public abstract List<TNode> getChildren();

    public TokenPosition getTokenPosition() {
        return tokenPos;
    }

    public abstract List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                          final TypestateAstValidator.Context ctx);

    public Map<String, Object> toJson() {
        var json = new LinkedHashMap<String, Object>();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.POS, tokenPos.toJson());
        return json;
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof TNode other &&
                other.tokenPos.equals(tokenPos);

    }
}
