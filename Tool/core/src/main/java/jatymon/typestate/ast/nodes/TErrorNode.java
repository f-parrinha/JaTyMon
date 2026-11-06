package jatymon.typestate.ast.nodes;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;

public class TErrorNode extends TNode {
    public static final String TYPE = "error";

    public TErrorNode(final TokenPosition tokenPos) {
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
}
