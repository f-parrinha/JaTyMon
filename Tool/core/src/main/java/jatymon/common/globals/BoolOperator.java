package jatymon.common.globals;

/**
 * Enum {@code BoolOperator} is an abstraction of basic boolean operators
 * @author Francisco Parrinha
 */
public enum BoolOperator {
    AND,
    OR,
    NEG;

    /**
     * Converts an operator as string to an {@code BoolOperator}
     *
     * @param value string operator
     * @return Operator enum
     */
    public static BoolOperator fromString(final String value) {
        return switch (value) {
            case GrammarTokens.AND -> AND;
            case GrammarTokens.OR -> OR;
            case GrammarTokens.NEG -> NEG;
            default -> throw new InvalidException(value);
        };
    }

    /**
     * Converts an {@code BoolOperator} enum to string format
     *
     * @param boolOperator operator enum
     * @return operator as string
     */
    public static String toString(final BoolOperator boolOperator) {
        return switch (boolOperator) {
            case AND -> GrammarTokens.AND;
            case OR -> GrammarTokens.OR;
            case NEG -> GrammarTokens.NEG;
        };
    }

    public static final class InvalidException extends RuntimeException {
        private static final String MESSAGE = "Invalid BoolOperator <%s>";
        public InvalidException(final String value) {
            super(MESSAGE.formatted(value));
        }
    }
}
