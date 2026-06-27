package jatymon.typestate.ast.nodes.key;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.diagnostics.semantic.internalstate.DuplicateFieldDiagnostic;
import jatymon.diagnostics.semantic.typestate.AmbiguousKeyDiagnostic;
import jatymon.diagnostics.semantic.typestate.KeyNotFoundInClassDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.TNode;

import javax.lang.model.element.Element;
import java.util.LinkedList;
import java.util.List;

public class TKeyNode extends TNode {
    private final String name;

    public TKeyNode(final String name, final TokenPosition tokenPos) {
        super(tokenPos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver, final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final String typestateName = ctx.getTypestateName();
        final TokenPosition tokenPos = getTokenPosition();

        final Element key = ctx.getTypestateClass().key();
        if (key == null || !key.getSimpleName().toString().equals(name))  {
            diagnostics.add(new KeyNotFoundInClassDiagnostic(name, ctx.getClassName(), typestateName, tokenPos));
        }
        if (ctx.containsField(name)) {
            diagnostics.add(new AmbiguousKeyDiagnostic(name, typestateName, tokenPos));
        }

        ctx.setKey(this);
        return diagnostics;
    }
}
