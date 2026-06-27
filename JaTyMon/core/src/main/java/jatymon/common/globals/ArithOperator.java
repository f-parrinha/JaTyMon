package jatymon.common.globals;

/**
 * Enum {@code ArithOperator} is an abstraction of basic arithmetic operators
 * @author Francisco Parrinha
 */
public enum ArithOperator {
    ADD,
    MULTIPLY,
    DIVIDE,
    SUB;

    /**
     * Converts an operator as string to an {@code ArithOperator}
     * @param value string operator
     * @return Operator enum
     */
    public static ArithOperator fromString(final String value) {
        return switch (value) {
            case GrammarTokens.SUM -> ADD;
            case GrammarTokens.SUB -> SUB;
            case GrammarTokens.MULTIPLY -> MULTIPLY;
            case GrammarTokens.DIVIDE -> DIVIDE;
            default -> throw new InvalidException(value);
        };
    }

    /**
     * Converts an {@code ArithOperator} enum to string format
     * @param arithOperator operator enum
     * @return operator as string
     */
    public static String toString(final ArithOperator arithOperator) {
        return switch (arithOperator) {
            case ADD -> GrammarTokens.SUM;
            case SUB -> GrammarTokens.SUB;
            case MULTIPLY -> GrammarTokens.MULTIPLY;
            case DIVIDE -> GrammarTokens.DIVIDE;
        };
    }

    public static final class InvalidException extends RuntimeException {
        private static final String MESSAGE = "Invalid ArithOperator <%s>";
        public InvalidException(final String value) {
            super(MESSAGE.formatted(value));
        }
    }
}
