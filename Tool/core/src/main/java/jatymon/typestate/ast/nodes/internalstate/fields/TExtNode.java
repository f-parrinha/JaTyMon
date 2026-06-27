package jatymon.typestate.ast.nodes.internalstate.fields;

import jatymon.diagnostics.semantic.internalstate.BadExtFieldTypeDiagnostic;
import jatymon.diagnostics.semantic.internalstate.ExtFieldNotFoundInClassDiagnostic;
import jatymon.resolving.Resolver;
import jatymon.common.globals.JsonKeys;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.internalstate.DuplicateFieldDiagnostic;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TExtNode extends TFieldNode {
    public static final String TYPE = "ext";
    public static final Set<TypeKind> ALLOWED_KINDS = Set.of(TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG);

    public TExtNode(final TokenPosition tokenPos, final String name) {
        super(tokenPos, name);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of();
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver, final TypestateAstValidator.Context ctx) {
        final String name = getName();
        final String fileName = ctx.getTypestateName();
        final TokenPosition tokenPos = getTokenPosition();
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        if (!ctx.addExt(name) || ctx.containsVal(name)) {
            diagnostics.add(new DuplicateFieldDiagnostic(name, ctx.getTypestateName(), tokenPos));
            return diagnostics;
        }

        // Check if found any element
        final Map<String, Element> annotatedExtFields = ctx.getTypestateClass().extFields();
        if (!annotatedExtFields.containsKey(name)) {
            diagnostics.add(new ExtFieldNotFoundInClassDiagnostic(name, ctx.getClassName(), fileName, tokenPos));
            return diagnostics;
        }

        // Validate KIND
        final TypeKind extKind = annotatedExtFields.get(name).asType().getKind();
        if (!ALLOWED_KINDS.contains(extKind)) {
            diagnostics.add(new BadExtFieldTypeDiagnostic(extKind, ALLOWED_KINDS, fileName, tokenPos));
        }
        return diagnostics;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TExtNode other &&
                super.equals(other);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, getName());
        return json;
    }
}
