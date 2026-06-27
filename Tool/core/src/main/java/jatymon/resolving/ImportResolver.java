package jatymon.resolving;

import jatymon.common.globals.JavaTypes;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.*;

class ImportResolver {
    private final Map<String, Boolean> resolvedPackagesCache;
    private final Map<String, String> qfNamesCache;
    private final Map<String, Set<String>> usedImportsCache;
    private final ProcessingEnvironment processingEnv;
    private final TypeResolver typeResolver;

    public ImportResolver(final ProcessingEnvironment processingEnv, final TypeResolver typeResolver) {
        this.qfNamesCache = new HashMap<>();
        this.resolvedPackagesCache = new HashMap<>();
        this.usedImportsCache = new HashMap<>();
        this.processingEnv = processingEnv;
        this.typeResolver = typeResolver;
    }

    /**
     * Returns whether the given package exists in the in code. The qualified name must be given (e.g. my.pkg)
     * @param qualifiedName package's qualified name
     * @return true if it exists, false if not
     */
    public boolean resolvePackage(final String qualifiedName) {
        return resolvedPackagesCache.computeIfAbsent(qualifiedName,k -> processingEnv.getElementUtils().getPackageElement(k) != null);
    }

    /**
     * Returns whether a given import exists in the in code. The qualified name must be given (e.g. my.pkg.myClass).
     * @param importStatement import's qualified name
     * @return if it has a wild card, whether the package exists. Otherwise, true if the type exists, false if not
     */
    public boolean resolveImport(final String importStatement) {
        final int dot = importStatement.lastIndexOf('.');
        return importStatement.endsWith(".*")
                ? resolvePackage(importStatement.substring(0, dot))
                : typeResolver.resolveType(importStatement);
    }

    /**
     * Receives a name (for a type) and qualifies it (if it is not a primitive type) according to the added imports. Since two typestates
     *  in the same package may import different types with the same name but in different packages, the key of the cache includes
     *  the typestate package and its name. This is also possible because in Java we cannot have two different import statements for types
     *  with the same name but in different packages
     * @param name name to qualify
     * @param imports set with imports to qualify from
     * @param typestatePkgName package qualified name of the typestate that requires resolving
     * @param typestateQfName name of the typestate that requires resolving
     * @return qualified name
     */
    public String qualifyName(final String name, final Set<String> imports, final String typestatePkgName, final String typestateQfName) {
        if (JavaTypes.isPrimitive(name)) {
            return name;
        }

        final String cacheKey = createQualifiedNamesCacheKey(name, typestateQfName);
        return qfNamesCache.computeIfAbsent(cacheKey, k -> {
            final StringBuilder res = new StringBuilder();
            final Stack<String> pending = new Stack<>();
            pending.add(name);
            while (!pending.isEmpty()) {
                final String current = pending.pop();
                if (current.equals(">") || current.equals(", ")) {
                    res.append(current);
                    continue;
                }

                // Add imported type to result and generics to the to "be processed" stack.
                final List<String> generics = GenericsResolver.extractGenerics(current);
                final String importMatch = qualifyFromImports(current, imports, typestatePkgName, typestateQfName);
                res.append(importMatch);
                if (!generics.isEmpty()) {
                    pending.push(">");
                    for (int i = generics.size() - 1; i >= 0; i--) {
                        pending.push(generics.get(i));
                        if (i > 0) pending.push(", ");
                    }
                    res.append("<");
                }
            }
            return res.toString();
        });
    }

    /**
     * Returns an unmodifiable set of the used imports for a given typestate (by its qualified name) stored in the {@code usedImportsCache}
     * @return unmodifiable set with used imports for a given typestate
     */
    public Set<String> getUsedImports(final String typestateQfName) {
        return Collections.unmodifiableSet(usedImportsCache.getOrDefault(typestateQfName, Set.of()));
    }


    /**
     * Creates a key for the {@code qualifiedNamesCache}
     * @param name name to qualify
     * @param typestateQfName typestate qualified name
     * @return cache key
     */
    private String createQualifiedNamesCacheKey(final String name, final String typestateQfName) {
        return "%s|%s".formatted(typestateQfName, name);
    }

    /**
     * TODO: Fix bug for the case where you have wild cards and both contain a class with the same name
     * Qualifies a name given a set of imports. If it did not manage to qualify, it either returns the name itself if
     *  already contains some kind of qualification, or the name qualified with the typestate's package name.
     * @param name name to qualify
     * @param imports set of imports as reference
     * @param typestatePkgName typestate package name (it is a default import statement)
     * @param typestateQfName typestate qualified name (to create a cache key)
     * @return qualified name
     */
    private String qualifyFromImports(final String name,
                                      final Set<String> imports,
                                      final String typestatePkgName,
                                      final String typestateQfName) {
        final Set<String> effectiveImports = new HashSet<>(imports);
        effectiveImports.add(typestatePkgName.concat(".*"));
        effectiveImports.add(JavaTypes.JavaLang.IMPORT);
        for (final String imp : effectiveImports) {
            String candidate = getImportMatchCandidate(name, imp);
            if (candidate != null && typeResolver.resolveType(candidate)) {
                usedImportsCache.computeIfAbsent(typestateQfName, k -> new HashSet<>()).add(imp);
                return candidate;
            }
        }

        final String erased = GenericsResolver.eraseGenerics(name);
        return erased.contains(".") ? erased : "%s.%s".formatted(typestatePkgName, erased);
    }

    /**
     * Finds an import that matches the given type (name)
     * @param name type to match
     * @param imp import to match
     * @return name if name == imp, qualified name or null if there is no match
     */
    private static String getImportMatchCandidate(final String name, final String imp) {
        final String erased = GenericsResolver.eraseGenerics(name);
        if (erased.equals(imp)) {
            return erased;
        }

        final String firstName = erased.contains(".") ? erased.substring(0, erased.indexOf(".")) : erased;
        if (imp.endsWith(".*")) {
            final String pkg = imp.substring(0, imp.length() - 2);  // Size of ".*" is 2!
            return "%s.%s".formatted(pkg, erased);
        } else {
            final int lastDot = imp.lastIndexOf('.');
            final String importTail = imp.substring(lastDot + 1);
            if (importTail.equals(firstName)) {
                final String importHead = imp.substring(0, lastDot);
                return "%s.%s".formatted(importHead, erased);
            }
        }
        return null;
    }
}
