package jatymon.typestate.ast.nodes.declaration;

import jatymon.common.JsonSerializable;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.imports.TImportNode;
import jatymon.typestate.ast.nodes.imports.TPackageNode;
import jatymon.typestate.ast.TypestateAstValidator;

import java.util.*;

public class TTypestateNode extends TNode {
    public static final String TYPE = "typestate";

    private final TPackageNode pkg;
    private final List<TImportNode> imports;
    private final TDeclarationNode declaration;

    public TTypestateNode(final TPackageNode pkg, final List<TImportNode> imports, final TDeclarationNode declaration) {
        // Gets the first node's token position
        super((pkg != null ? pkg : imports != null && !imports.isEmpty() ? imports.getFirst() : declaration).getTokenPosition());

        this.pkg = pkg;
        this.imports = List.copyOf(imports);
        this.declaration = declaration;
    }

    public TPackageNode getPkg() {
        return pkg;
    }

    public List<TImportNode> getImports() {
        return imports;
    }

    public TDeclarationNode getDeclaration() {
        return declaration;
    }

    @Override
    public List<TNode> getChildren() {
        List<TNode> res = new ArrayList<>();
        if (this.pkg != null) {
            res.add(this.pkg);
        }
        res.addAll(this.imports);
        res.add(this.declaration);
        return res;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        return List.of();
    }

    @Override
    public String toString() {
        return String.format("[TTypestateNode {pkg: %s, imports: %s, declaration: %s}]", pkg, imports, declaration);
    }

    @Override
    public boolean equals(final Object obj) {
        return  obj instanceof TTypestateNode other &&
                super.equals(other) &&
                other.declaration.equals(declaration) &&
                other.imports.equals(imports) &&
                Objects.equals(other.pkg, pkg);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        if (pkg != null)
            json.put(JsonKeys.PKG, pkg.toJson());
        json.put(JsonKeys.IMPORTS, JsonSerializable.fromList(imports));
        json.put(JsonKeys.DECL, declaration.toJson());
        return json;
    }
}

