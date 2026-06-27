package jatymon.typestate.ast.nodes.imports;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.ref.TRefNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.resolving.DuplicateImportDiagnostic;
import jatymon.diagnostics.semantic.resolving.ImportCannotBeResolvedDiagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TImportNode extends TNode {
    public static final String TYPE = "import";

    private final TRefNode ref;
    private final Boolean isStatic;
    private final Boolean hasStar;

    public TImportNode(final TokenPosition tokenPos, final TRefNode ref, final Boolean isStatic, final Boolean hasStar) {
        super(tokenPos);
        this.ref = ref;
        this.isStatic = isStatic;
        this.hasStar = hasStar;
    }

    /**
     * Returns the full name of the import. It does not include wildcards ({@code *})
     * @return import statement full name without wildcard
     */
    public String getQualifiedName() {
        return ref.getFullName();
    }

    public TRefNode getRef() {
        return ref;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public Boolean getHasStar() {
        return hasStar;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final String qualifiedName = getQualifiedName();
        final String protocolName = ctx.getTypestateName();
        final TokenPosition tokenPos = getTokenPosition();

        String toAddToImport = qualifiedName;
        if (hasStar) {
            toAddToImport = qualifiedName.concat(".*");
            if (!resolver.resolvePackage(qualifiedName)) {
                return List.of(new ImportCannotBeResolvedDiagnostic(protocolName, tokenPos, qualifiedName));
            }
        } else if (!resolver.resolveImport(qualifiedName)) {
            return List.of(new ImportCannotBeResolvedDiagnostic(protocolName, tokenPos, qualifiedName));
        }

        boolean added = ctx.addImport(toAddToImport);
        return added ? List.of() : List.of(new DuplicateImportDiagnostic(protocolName, tokenPos, toAddToImport));
    }

    @Override
    public List<TNode> getChildren() {
        List<TNode> res = new ArrayList<>();
        res.add(ref);
        return res;
    }

    @Override
    public String toString() {
        return String.format("[TImportNode {isStatic: %s, hasStar: %s}]", isStatic, hasStar);
    }

    @Override
    public boolean equals(final Object obj) {
        return  obj instanceof TImportNode other &&
                super.equals(other) &&
                other.hasStar.equals(hasStar) &&
                other.isStatic.equals(isStatic) &&
                other.ref.equals(ref);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.REF, ref.toJson());
        json.put(JsonKeys.STATIC, isStatic);
        json.put(JsonKeys.STAR, hasStar);
        return json;
    }


}

