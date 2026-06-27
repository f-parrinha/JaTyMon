package jatymon.typestate.ast.nodes.internalstate.assignments;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.DuplicateAssignmentDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TAssignNode extends TNode {
    public static final String TYPE = "assign";

    private final String name;
    private final TAssignExprNode assignExpr;

    public TAssignNode(final TokenPosition tokenPos, final String name, final TAssignExprNode assignExpr) {
        super(tokenPos);
        this.name = name;
        this.assignExpr = assignExpr;
    }

    public TAssignExprNode getAssignExpr() {
        return assignExpr;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(assignExpr);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        if (!ctx.addAssignment(this)) {
            diagnostics.add(new DuplicateAssignmentDiagnostic(name, ctx.getTypestateName(), getTokenPosition()));
        }
        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TAssignNode other &&
                super.equals(other) &&
                other.assignExpr.equals(assignExpr) &&
                other.name.equals(name);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        json.put(JsonKeys.ASSIGN_EXPR, assignExpr.toJson());
        return json;
    }
}
