package jatymon.resolving;

import jatymon.exceptions.UnknownEnumException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

/**
 * Class {@code Resolver} holds a reference to a ProcessingEnvironment (Annotation API), offering methods to search for types,
 *  packages and other java resources. Helpful to translate and search Java in code.
 *
 *  <p>
 *      The resolver should be setup during the typestate annotation processor's execution, collecting a processing environment at that moment.
 *  </p>
 */
public class Resolver {
    private final TypeResolver typeResolver;
    private final ImportResolver importResolver;
    private final MethodResolver methodResolver;
    private final ProcessingEnvironment processingEnv;

    public Resolver(final ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.typeResolver = new TypeResolver(processingEnv);
        this.importResolver = new ImportResolver(processingEnv, this.typeResolver);
        this.methodResolver = new MethodResolver(processingEnv, this.typeResolver);
    }

    /**
     * Returns whether a given type exists in the in code. The qualified name must be given (e.g. my.pkg.myClass)
     * @param qualifiedName type's qualified name
     * @return true if it exists, false if not
     */
    public boolean resolveType(final String qualifiedName) {
        return typeResolver.resolveType(qualifiedName);
    }

    /**
     * Returns the TypeElement for the given qualified name. If it is primitive it returns null
     * @param qualifiedName type's qualified name
     * @return TypeElement if it exists
     */
    public TypeElement resolveTypeElement(final String qualifiedName) {
        return typeResolver.resolveTypeElement(qualifiedName);
    }

    /**
     * Resolved the given qualified name and returns (if it exists) the corresponding TypeMirror from the source code
     * @param qualifiedName qualified name of the type to resolve
     * @return TypeMirror or null
     */
    public TypeMirror resolveTypeMirror(final String qualifiedName) {
        return typeResolver.resolveTypeMirror(qualifiedName);
    }

    /**
     * Returns whether the given qualified name is an enum type or a boolean
     * @param qualifiedName qualified name for the enum type or a boolean
     * @return true if boolean or enum type exists
     */
    public boolean resolveEnum(final String qualifiedName) {
        return typeResolver.resolveEnum(qualifiedName);
    }

    /**
     * Returns the set of values for a given enum. If the given enum name does not exist, the method returns an empty set. The enum
     *  name may also be 'boolean'. In that case, the method returns a set contains 'true' and 'false'.
     * @param qualifiedName the enum's qualified name or {@code boolean}
     * @return {@code {true, false}} if {@code qualifiedName} is {@code boolean}, enum's values, or empty state if enum does not exist.
     */
    public Set<String> getEnumValues(final String qualifiedName) throws UnknownEnumException {
        return typeResolver.getEnumValues(qualifiedName);
    }

    /**
     * Returns whether the given package exists in the in code. The qualified name must be given (e.g. my.pkg)
     * @param qualifiedName package's qualified name
     * @return true if it exists, false if not
     */
    public boolean resolvePackage(final String qualifiedName) {
        return importResolver.resolvePackage(qualifiedName);
    }

    /**
     * Returns whether a given import exists in the in code. The qualified name must be given (e.g. my.pkg.myClass).
     * @param qualifiedName import's qualified name
     * @return if it has a wild card, whether the package exists. Otherwise, true if the type exists, false if not
     */
    public boolean resolveImport(final String qualifiedName) {
        return importResolver.resolveImport(qualifiedName);
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
        return importResolver.qualifyName(name, imports, typestatePkgName, typestateQfName);
    }

    /**
     * Returns an unmodifiable set of the used imports for a given typestate (by its qualified name) stored in the {@code usedImportsCache}
     * @return unmodifiable set with used imports for a given typestate
     */
    public Set<String> getUsedImports(final String typestateQfName) {
        return importResolver.getUsedImports(typestateQfName);
    }

    /**
     * Returns whether a method given its name and arguments exists in a class with the given qualified name
     * @param classQfName       qualified name of the reference class
     * @param methodName  simple name of the method
     * @param argsTypesQfNames  names for the arguments of the method
     * @return true if it exists in the class, false if not
     */
    public boolean resolveMethod(final String classQfName,
                                 final String methodName,
                                 final String returnTypeQfName,
                                 final List<String> argsTypesQfNames) {
        return methodResolver.resolveMethod(classQfName, methodName, returnTypeQfName, argsTypesQfNames);
    }

    /**
     * Returns a list with all throwable elements belonging to a method in a class
     * @param classQfName class qualified name
     * @param methodName method name
     * @param returnTypeQfName return type qualified name
     * @param argsTypesQfNames list with the qualified names of the method's arguments
     * @return list with throwable elements of a method
     */
    public List<? extends TypeMirror> getThrownTypes(final String classQfName,
                                         final String methodName,
                                         final String returnTypeQfName,
                                         final List<String> argsTypesQfNames) {
        return methodResolver.getThrownTypes(classQfName, methodName, returnTypeQfName, argsTypesQfNames);
    }

    /**
     * Returns the currently in used ProcessingEnvironment instance from the Java Annotation Processor
     * @return ProcessingEnvironment instance in use
     */
    public final ProcessingEnvironment getProcessingEnv() {
        return processingEnv;
    }
}
