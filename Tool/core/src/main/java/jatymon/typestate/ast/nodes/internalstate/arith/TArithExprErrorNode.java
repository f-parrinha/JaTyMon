package jatymon.typestate.ast.nodes.internalstate.arith;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import java.util.List;

public class TArithExprErrorNode extends TArithExprNode {
    public static final String TYPE = "arithExprError";

    public TArithExprErrorNode(final TokenPosition tokenPos) {
        super(tokenPos);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(Resolver resolver, TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toCode() {
        return "";
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof TArithExprErrorNode;
    }
}
