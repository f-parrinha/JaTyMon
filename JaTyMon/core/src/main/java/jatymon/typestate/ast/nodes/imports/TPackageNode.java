package jatymon.typestate.ast.nodes.imports;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.ref.TRefNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.resolving.PackageCannotBeResolvedDiagnostic;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TPackageNode extends TNode {
    private static final String TYPE = "package";

    private final TRefNode ref;

    public TPackageNode(final TokenPosition tokenPos, final TRefNode ref) {
        super(tokenPos);
        this.ref = ref;
    }

    public String getQualifiedName() {
        return ref.getFullName();
    }

    public TRefNode getRef() {
        return ref;
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(ref);
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final String pkgName = getQualifiedName();
        ctx.setPackage(pkgName);
        if (!resolver.resolvePackage(pkgName)) {
            diagnostics.add(new PackageCannotBeResolvedDiagnostic(ctx.getTypestateName(), getTokenPosition(), pkgName));
        }
        return diagnostics;
    }

    @Override
    public String toString() {
        return String.format("[TPackageNode {ref: %s}]", ref);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TPackageNode other &&
                super.equals(other) &&
                other.ref.equals(ref);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.REF, ref.toJson());
        return json;
    }
}
