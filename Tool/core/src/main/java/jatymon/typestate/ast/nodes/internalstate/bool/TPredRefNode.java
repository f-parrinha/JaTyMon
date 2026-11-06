package jatymon.typestate.ast.nodes.internalstate.bool;

import jatymon.common.TokenPosition;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.diagnostics.semantic.internalstate.UndefinedPredicateDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TPredRefNode extends TBoolExprNode {
    public static final String TYPE = "predRef";

    private final String name;

    public TPredRefNode(final TokenPosition tokenPos, final String name) {
        super(tokenPos);
        this.name = name;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver, final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        if (!ctx.containsPredicate(name)) {
            diagnostics.add(new UndefinedPredicateDiagnostic(name, ctx.getTypestateName(), getTokenPosition()));
        }
        return diagnostics;
    }

    @Override
    public String toCode() {
        return "%s()".formatted(name);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TPredRefNode other &&
                super.equals(other) &&
                other.name.equals(name);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        return json;
    }

    public String getName() {
        return name;
    }
}
