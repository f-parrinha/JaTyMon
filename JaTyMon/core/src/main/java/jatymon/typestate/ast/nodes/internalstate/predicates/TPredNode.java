package jatymon.typestate.ast.nodes.internalstate.predicates;

import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.DuplicatePredicateDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TPredNode extends TNode {
    public static final String TYPE = "pred";

    private final String name;
    private final TBoolExprNode boolExpr;

    public TPredNode(final TokenPosition tokenPos, final String name, final TBoolExprNode boolExpr) {
        super(tokenPos);
        this.name = name;
        this.boolExpr = boolExpr;
    }

    public String getName() {
        return name;
    }

    public TBoolExprNode getBoolExpr() {
        return boolExpr;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(boolExpr);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        if (!ctx.addPredicate(this)) {
            diagnostics.add(new DuplicatePredicateDiagnostic(name, ctx.getTypestateName(), getTokenPosition()));
        }
        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TPredNode other &&
                super.equals(other) &&
                other.name.equals(name) &&
                other.boolExpr.equals(boolExpr);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        json.put(JsonKeys.BOOL_EXPR, boolExpr.toJson());
        return json;
    }
}
