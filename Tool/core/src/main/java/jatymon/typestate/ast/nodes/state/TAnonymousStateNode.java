package jatymon.typestate.ast.nodes.state;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.action.TActionNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.List;

public class TAnonymousStateNode extends TStateNode {
    public static final String TYPE = "anonymous";
    public TAnonymousStateNode(final TokenPosition tokenPos,
                               final List<TActionNode> inputActions,
                               final List<TActionNode> outputActions) {
        super(tokenPos, String.format("%s::%s", TYPE, tokenPos), inputActions, outputActions);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        var prevResult = super.validateSelf(resolver, ctx);
        ctx.addState(this);
        return prevResult;
    }

    @Override
    public String toString() {
        return String.format("[TAnonymousNode {tokenPos: %s, inputActions: %s, outputActions: %s}]", getTokenPosition(), inputActions, outputActions);
    }

    @Override
    public boolean equals(final Object obj) {
        return  obj instanceof TStateNode other &&
                super.equals(other);
    }
}
