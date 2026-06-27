package jatymon.typestate;

import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TValNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.TPredNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.key.TKeyNode;
import jatymon.typestate.graph.Graph;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TypestateData {
    private final Graph graph;
    private final String name;
    private final TKeyNode key;
    private final String className;
    private final String classQfName;
    private final String packageName;
    private final String qualifiedName;
    private final Set<String> imports;
    private final Set<String> exts;
    private final Map<String, TAssignNode> assignments;
    private final Map<String, TPredNode> predicates;
    private final Map<String, TValNode> vals;

    public TypestateData(final Graph graph, final TypestateAstValidator.Context ctx) {
        this.graph = graph;
        this.name = ctx.getTypestateName();
        this.key = ctx.getKey();
        this.qualifiedName = ctx.getTypestateQfName();
        this.className = ctx.getClassName();
        this.classQfName = ctx.getClassQfName();
        this.packageName = ctx.getPackageQfName();
        this.imports = ctx.getImports();
        this.assignments = ctx.getAssignments();
        this.predicates = ctx.getPredicates();
        this.exts = ctx.getExts();
        this.vals = ctx.getVals();
    }

    public Graph getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }

    public TKeyNode getKey() {
        return key;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getClassName() {
        return className;
    }

    public String getClassQfName() {
        return classQfName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Set<String> getImports() {
        return imports;
    }

    public Set<String> getExts() {
        return exts;
    }

    public Set<TValNode> getVals() {
        return Set.copyOf(vals.values());
    }

    public Set<TAssignNode> getAssignments() {
        return Set.copyOf(assignments.values());
    }

    public Set<TPredNode> getPredicates() {
        return Set.copyOf(predicates.values());
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TypestateData other && this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, name, className, packageName, imports, assignments, predicates, exts, vals);
    }
}
