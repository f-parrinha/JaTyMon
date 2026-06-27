package jatymon.resolving;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.*;

class MethodResolver {
    private static final Set<Modifier> NON_OVERRIDABLE_MODIFIERS = Set.of(Modifier.PRIVATE, Modifier.STATIC);

    private final Map<String, ExecutableElement> resolvedMethodsCache;
    private final TypeResolver typeResolver;
    private final ProcessingEnvironment processingEnv;

    public MethodResolver(final ProcessingEnvironment processingEnv, final TypeResolver typeResolver) {
        this.resolvedMethodsCache = new HashMap<>();
        this.typeResolver = typeResolver;
        this.processingEnv = processingEnv;
    }
    /**
     * Returns whether a method given its name and arguments exists in a class with the given qualified name
     * @param classQfName       qualified name of the reference class
     * @param methodSimpleName  simple name of the method
     * @param argsTypesQfNames  names for the arguments of the method
     * @return true if it exists in the class, false if not
     */
    public boolean resolveMethod(final String classQfName,
                                 final String methodSimpleName,
                                 final String returnTypeQfName,
                                 final List<String> argsTypesQfNames) {
        return findMethod(classQfName, methodSimpleName, returnTypeQfName, argsTypesQfNames) != null;
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
        final ExecutableElement element = findMethod(classQfName, methodName, returnTypeQfName, argsTypesQfNames);
        return element == null ? List.of() : element.getThrownTypes();
    }

    /**
     * Creates a key for the {@code resolvedMethodsCache}
     * @param classQfName class qualified name containing the method
     * @param methodSimpleName method's simple name
     * @param returnTypeQfName return type qualified name type
     * @param argTypeNames list with type names of the method's parameters
     * @return cache key
     */
    private String createResolvedMethodsCacheKey(final String classQfName,
                                                 final String methodSimpleName,
                                                 final String returnTypeQfName,
                                                 final List<String> argTypeNames) {
        return "%s|%s|%s|%s".formatted(classQfName, methodSimpleName, returnTypeQfName, String.join(",", argTypeNames));
    }


    /**
     * Finds a method in the source code
     * @param classQfName class qualified name
     * @param methodName method name
     * @param returnTypeQfName return type qualified name
     * @param argsTypesQfNames list with the qualified names of the method's arguments
     * @return {@code ExecutableElement} representing the found method or null
     */
    private ExecutableElement findMethod(final String classQfName,
                                         final String methodName,
                                         final String returnTypeQfName,
                                         final List<String> argsTypesQfNames) {
        final String cacheKey = createResolvedMethodsCacheKey(classQfName, methodName, returnTypeQfName, argsTypesQfNames);
        if (resolvedMethodsCache.containsKey(cacheKey)) {
            return resolvedMethodsCache.get(cacheKey);
        }

        final Types typeUtils = processingEnv.getTypeUtils();
        TypeElement classTypeElement = typeResolver.resolveTypeElement(classQfName);
        while (classTypeElement != null) {
            for (final ExecutableElement method : ElementFilter.methodsIn(classTypeElement.getEnclosedElements())) {
                final boolean notStaticNorPrivate = Collections.disjoint(method.getModifiers(), NON_OVERRIDABLE_MODIFIERS);
                final boolean matchReturnTypes = typeResolver.hasMatchingType(method.getReturnType(), returnTypeQfName);
                final boolean matchParams = hasMatchingParameters(method, argsTypesQfNames);
                final boolean matchNames = method.getSimpleName().contentEquals(methodName);
                if (notStaticNorPrivate && matchNames && matchReturnTypes && matchParams) {
                    resolvedMethodsCache.put(cacheKey, method);
                    return method;
                }
            }
            // Walk up to the superclass
            final TypeMirror superclassMirror = classTypeElement.getSuperclass();
            if (superclassMirror.getKind() == TypeKind.NONE || superclassMirror.getKind() == TypeKind.ERROR) {
                break;
            } else {
                classTypeElement = (TypeElement) typeUtils.asElement(superclassMirror);
            }
        }
        return null;
    }

    /**
     * Checks if a method's parameters matches the given type names. Uses {@code TypeMirror} which means it considers generic types.
     * @param method method {@code ExecutableElement}
     * @param argTypeNames list with the different type names for the method's parameters
     * @return true or false
     */
    private boolean hasMatchingParameters(final ExecutableElement method, final List<String> argTypeNames) {
        final List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() != argTypeNames.size()) {
            return false;
        }

        for (int i = 0; i < parameters.size(); i++) {
            final TypeMirror paramType = parameters.get(i).asType();
            if(!typeResolver.hasMatchingType(paramType, argTypeNames.get(i))) {
                return false;
            }
        }
        return true;
    }
}
