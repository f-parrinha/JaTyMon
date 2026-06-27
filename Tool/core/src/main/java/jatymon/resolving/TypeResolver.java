package jatymon.resolving;

import jatymon.exceptions.UnknownEnumException;
import jatymon.common.globals.JavaTypes;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

class TypeResolver {
    private static final Set<String> ENUM_BOOLEAN_VALUES = Set.of("true", "false");

    private final Map<String, TypeElement> resolvedTypesCache;
    private final ProcessingEnvironment processingEnv;

    public TypeResolver(final ProcessingEnvironment processingEnv) {
        this.resolvedTypesCache = new HashMap<>();
        this.processingEnv = processingEnv;
    }

    /**
     * Returns whether a given type exists in the in code. The qualified name must be given (e.g. my.pkg.myClass)
     * @param qualifiedName type's qualified name
     * @return true if it exists, false if not
     */
    public boolean resolveType(final String qualifiedName) {
        return JavaTypes.isPrimitive(qualifiedName) ||
                JavaTypes.isJavaLang(qualifiedName) ||
                resolveTypeElement(qualifiedName) != null;
    }

    /**
     * TODO: Support wild cards in generics (also requires support in the grammar)
     * TODO: Support arrays in the generics
     * Returns the TypeElement for the given qualified name. If it is primitive it returns null. The method takes generics
     *  into consideration.
     * @param qualifiedName type's qualified name
     * @return TypeElement if it exists
     */
    public TypeElement resolveTypeElement(final String qualifiedName) {
        final Queue<String> pending = new LinkedList<>();
        pending.add(qualifiedName);
        TypeElement rootEl = null;
        while (!pending.isEmpty()) {
            final String current = pending.poll();
            final TypeElement typeElement = findTypeElement(current);
            if (typeElement == null) {
                return null;
            } else if (rootEl == null) {
                rootEl = typeElement;
            }

            final List<String> currentGenerics = GenericsResolver.extractGenerics(current);
            if (GenericsResolver.checkArity(currentGenerics, typeElement)) {
                pending.addAll(currentGenerics);
            } else if (!currentGenerics.isEmpty()) {
                return null;
            }
        }
        return rootEl;
    }

    /**
     * Resolved the given qualified name and returns (if it exists) the corresponding TypeMirror from the source code.
     *  The method takes generics into consideration.
     * @param qualifiedName qualified name of the type to resolve
     * @return TypeMirror or null
     */
    public TypeMirror resolveTypeMirror(final String qualifiedName) {
        final boolean isPrimitive = JavaTypes.isPrimitive(qualifiedName);
        final Types typeUtils = processingEnv.getTypeUtils();
        if (isPrimitive && qualifiedName.equals(JavaTypes.Primitives.VOID)) {
            return typeUtils.getNoType(TypeKind.VOID);
        } else if (isPrimitive) {
            return typeUtils.getPrimitiveType(TypeKind.valueOf(qualifiedName.toUpperCase()));
        }

        final Stack<TypeBlueprint> processingStack = buildTypeBlueprintStack(qualifiedName);
        if (processingStack == null) {
            return null;
        }

        return assembleTypeMirrors(processingStack, qualifiedName);
    }

    /**
     * Returns whether the given qualified name is an enum type or a boolean
     * @param qualifiedName qualified name for the enum type or a boolean
     * @return true if boolean or enum type exists
     */
    public boolean resolveEnum(final String qualifiedName) {
        if (JavaTypes.isBoolean(qualifiedName)) return true;

        final TypeElement typeElement = findTypeElement(qualifiedName);
        return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
    }


    /**
     * Checks if a type matches the given type qualified name
     * @param matchingType actual type (mirror) in the source code
     * @param typeQfName type qualified name
     * @return type matches the given type qualified name (they are the same type)
     */
    public boolean hasMatchingType(final TypeMirror matchingType, final String typeQfName) {
        final TypeMirror expectedType = resolveTypeMirror(typeQfName);
        if (expectedType == null) {
            return false;
        }

        final Types typeUtils = processingEnv.getTypeUtils();
        return typeUtils.isSameType(matchingType, expectedType) ||
                (GenericsResolver.extractGenerics(typeQfName).isEmpty() &&
                        typeUtils.isSameType(typeUtils.erasure(matchingType), typeUtils.erasure(expectedType)));
    }

    /**
     * Returns the set of values for a given enum. If the given enum name does not exist, the method returns an empty set. The enum
     *  name may also be 'boolean'. In that case, the method returns a set contains 'true' and 'false'.
     * @param qualifiedName the enum's qualified name or {@code boolean}
     * @return {@code {true, false}} if {@code qualifiedName} is {@code boolean}, enum's values, or empty state if enum does not exist.
     */
    public Set<String> getEnumValues(final String qualifiedName) throws UnknownEnumException {
        if (JavaTypes.isBoolean(qualifiedName)) {
            return ENUM_BOOLEAN_VALUES;
        }

        final TypeElement typeElement = findTypeElement(qualifiedName);
        if (typeElement == null || typeElement.getKind() != ElementKind.ENUM) {
            throw new UnknownEnumException(qualifiedName);
        }

        return typeElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.ENUM_CONSTANT)
                .map(Element::getSimpleName)
                .map(Object::toString)
                .collect(Collectors.toSet());
    }


    /**
     * Finds a {@code TypeElement} instance given a qualified name in the source code and manages the types caches.<br><br>
     * The method erases the qualified name. For {@code TypeElements} we do not need to consider the generics. <br><br>
     * Note if a type is not found it caches the erased name paired with null. This is good because an unresolvable type
     *  may never become resolvable.
     * @param qualifiedName type qualified name
     * @return TypeElement or null if not found
     */
    private TypeElement findTypeElement(final String qualifiedName) {
        final String erasedName = GenericsResolver.eraseGenerics(qualifiedName);
        if (resolvedTypesCache.containsKey(erasedName)) {
            return resolvedTypesCache.get(erasedName);
        }

        final TypeElement res = processingEnv.getElementUtils().getTypeElement(erasedName);
        resolvedTypesCache.put(erasedName, res);
        return res;
    }

    /**
     * Returns a stack with type mirror blueprints for building {@code TypeMirror} instances from a qualified name that may include generics
     * <p> PRE: {@code rootName} is not a primitive nor {@code void} </p>
     * @param rootName qualified name
     * @return Stack with search frames for creating {@code TypeMirror} instances
     */
    private Stack<TypeBlueprint> buildTypeBlueprintStack(final String rootName) {
        final Stack<TypeBlueprint> qfNames = new Stack<>();
        final Queue<String> pending = new LinkedList<>(List.of(rootName));
        while (!pending.isEmpty()) {
            final String current = pending.poll();
            if (JavaTypes.isPrimitive(current) || current.equals(JavaTypes.Primitives.VOID)) {
                return null;    // There cannot be primitives nor void in a generic
            }

            final List<String> currentGenerics = GenericsResolver.extractGenerics(current);
            pending.addAll(currentGenerics);
            qfNames.push(new TypeBlueprint(current, GenericsResolver.eraseGenerics(current), currentGenerics));
        }
        return qfNames;
    }

    /**
     * Assembles a {@code TypeMirror} instance given a stack of type mirror blueprints
     * <p> PRE: {@code rootName} is not a primitive nor {@code void} </p>
     * @param typeBluprintStack stack with search frames for assembling a {@code TypeMirror} instance
     * @param rootName root qualified name
     * @return {@code TypeMirror} instance or null if there is none that can be found in the source code
     */
    private TypeMirror assembleTypeMirrors(final Stack<TypeBlueprint> typeBluprintStack, final String rootName) {
        final Types typeUtils = processingEnv.getTypeUtils();
        final Map<String, TypeMirror> builtMirrors = new HashMap<>();
        while (!typeBluprintStack.isEmpty()) {
            final TypeBlueprint typeBlueprint = typeBluprintStack.pop();
            final TypeElement typeEl = findTypeElement(typeBlueprint.erasedQfName());
            if (typeEl == null) {
                return null;
            } else if (typeBlueprint.generics().isEmpty()) {
                builtMirrors.put(typeBlueprint.qfName(), typeUtils.erasure(typeEl.asType()));
            } else {
                final TypeMirror[] argMirrors = new TypeMirror[typeBlueprint.generics().size()];
                for (int i = 0; i < typeBlueprint.generics().size(); i++) {
                    argMirrors[i] = builtMirrors.get(typeBlueprint.generics().get(i));
                }
                builtMirrors.put(typeBlueprint.qfName(), typeUtils.getDeclaredType(typeEl, argMirrors));
            }
        }
        return builtMirrors.get(rootName);
    }

    /**
     * A structure for building type mirrors recursively.
     * It represents a section within the tree of generics in a generic type
     * @param qfName type qualified name
     * @param erasedQfName erased name
     * @param generics list with the generics type names
     */
    private record TypeBlueprint(String qfName, String erasedQfName, List<String> generics) {}
}
