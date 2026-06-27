package jatymon.common.globals;

/**
 * Class {@code Types} holds constants for java types as string values ("void", "int", ...)
 * @author Francisco Parrinha
 */
public class JavaTypes {

    /**
     * Class {@code Primitives} contains constant names for primitives
     * @author Francisco Parrinha
     */
    public static final class Primitives {
        public static final String VOID = "void";
        public static final String BOOLEAN = "boolean";
        public static final String BYTE = "byte";
        public static final String SHORT = "short";
        public static final String INT = "int";
        public static final String LONG = "long";
        public static final String FLOAT = "float";
        public static final String DOUBLE = "double";
        public static final String CHAR = "char";
    }

    /**
     * Class {@code JavaLang} contains constant names for implicitly imported java lang objects
     * @author Francisco Parrinha
     */
    public static final class JavaLang {
        public static final String OBJECT = "Object";
        public static final String CLASS = "Class";
        public static final String NUMBER = "Number";
        public static final String VOID = "Void";
        public static final String BOOLEAN = "Boolean";
        public static final String BYTE = "Byte";
        public static final String SHORT = "Short";
        public static final String INTEGER = "Integer";
        public static final String LONG = "Long";
        public static final String FLOAT = "Float";
        public static final String DOUBLE = "Double";
        public static final String CHARACTER = "Character";
        public static final String STRING = "String";
        public static final String IMPORT = "java.lang.*";
    }

    public static boolean isBoolean(String name) {
        return name.equals(Primitives.BOOLEAN) || name.equals(JavaLang.BOOLEAN);
    }

    public static boolean isPrimitive(final String name) {
        return name.equals(Primitives.VOID)
                || name.equals(Primitives.BOOLEAN)
                || name.equals(Primitives.BYTE)
                || name.equals(Primitives.SHORT)
                || name.equals(Primitives.INT)
                || name.equals(Primitives.LONG)
                || name.equals(Primitives.FLOAT)
                || name.equals(Primitives.DOUBLE)
                || name.equals(Primitives.CHAR);
    }

    public static boolean isJavaLang(final String typeName) {
        final String baseType = typeName.replaceAll("<.*>", "")
                .replaceAll("\\[\\]", "");
        return baseType.equals(JavaLang.OBJECT)
                || baseType.equals(JavaLang.STRING)
                || baseType.equals(JavaLang.CLASS)
                || baseType.equals(JavaLang.INTEGER)
                || baseType.equals(JavaLang.LONG)
                || baseType.equals(JavaLang.DOUBLE)
                || baseType.equals(JavaLang.FLOAT)
                || baseType.equals(JavaLang.BOOLEAN)
                || baseType.equals(JavaLang.CHARACTER)
                || baseType.equals(JavaLang.BYTE)
                || baseType.equals(JavaLang.SHORT)
                || baseType.equals(JavaLang.NUMBER)
                || baseType.equals(JavaLang.VOID);
    }

    public static Class<?> getPrimitiveClass(final String name) {
        return switch (name) {
            case Primitives.VOID -> void.class;
            case Primitives.BOOLEAN -> boolean.class;
            case Primitives.BYTE -> byte.class;
            case Primitives.SHORT -> short.class;
            case Primitives.INT -> int.class;
            case Primitives.LONG -> long.class;
            case Primitives.FLOAT -> float.class;
            case Primitives.DOUBLE -> double.class;
            case Primitives.CHAR -> char.class;
            default -> throw new NotPrimitiveException();
        };
    }

    private static class NotPrimitiveException extends RuntimeException {
        public static final String MESSAGE = "The given name is not a primitive type name in the Java language.";
        public NotPrimitiveException() {
            super(MESSAGE);
        }
    }
}
