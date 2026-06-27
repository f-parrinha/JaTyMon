package jatymon.resolving;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

public class GenericsResolver {

    /**
     * Checks if a given type name is generic or not
     * @param typeName type name
     * @return type name is generic
     */
    public static boolean isGeneric(final String typeName) {
        return typeName.contains("<") && typeName.contains(">");
    }

    /**
     * Checks if a given source type element is generic or not
     * @param typeElement source type element
     * @return source type element is generic
     */
    public static boolean isGeneric(final TypeElement typeElement) {
        return !typeElement.getTypeParameters().isEmpty();
    }

    /**
     * Performs an arity check between a type name and its corresponding source type element
     * @param typeName type name
     * @param sourceType corresponding source type element
     * @return arity is the same
     */
    public static boolean checkArity(final String typeName, TypeElement sourceType) {
        return sourceType.getTypeParameters().size() == GenericsResolver.extractGenerics(typeName).size();
    }

    /**
     * Performs an arity check between a list of generic type names belonging to a type name and its corresponding source type element
     * @param genericsTypeNames list with generic args
     * @param sourceType corresponding source type element
     * @return arity is the same
     */
    public static boolean checkArity(final List<String> genericsTypeNames, TypeElement sourceType) {
        return sourceType.getTypeParameters().size() == genericsTypeNames.size();
    }

    /**
     * Removes the generic section of a type name
     * @param name type name
     * @return erased type name
     */
    public static String eraseGenerics(final String name) {
        final int idx = name.indexOf('<');
        return idx == -1 ? name : name.substring(0, idx).strip();
    }

    /**
     * Returns a list containing the generic types in a type name. The generic types may be generic as well. This means
     *  calling {@code extractiGenericArgs("Type<One<Two, Thre>, Four>")} should return {@code [One<Two, Three>, Four]}
     * @param name type name
     * @return list with the generic types in a name (or empty list)
     */
    public static List<String> extractGenerics(final String name) {
        final int open = name.indexOf('<');
        final int close = name.lastIndexOf('>');
        if (open == -1 || close == -1) return List.of();

        // Use depth because inner types may have generics as well, e.g., A<T<B,C>,D> should return a list with T<B,C> and D
        final List<String> args = new ArrayList<>();
        int depth = 0;
        int start = open + 1;
        for (int i = open + 1; i <= close; i++) {
            final char c = name.charAt(i);
            if (c == '<') {
                depth++;
            } else if (c == '>') {
                depth--;
            } else if (c == ',' && depth == 0) {
                args.add(name.substring(start, i).strip());
                start = i + 1;
            }
        }
        args.add(name.substring(start, close).strip());
        return args;
    }
}
