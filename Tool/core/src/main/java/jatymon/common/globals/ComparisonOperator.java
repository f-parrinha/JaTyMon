package jatymon.common.globals;

/**
 * Enum {@code ComparisionOperator} contains operators such as {@code =}, {@code !=}, {@code >}, {@code <}, etc...
 * @author Francisco Parrinha
 */
public enum ComparisonOperator {
    EQ,
    NEQ,
    GT,
    GEQ,
    LT,
    LEQ;

    static final String EQ_CODE = "==";


    /**
     * Converts an operator as string to an {@code ComparisionOperator}
     *
     * @param value string operator
     * @return Operator enum
     */
    public static ComparisonOperator fromString(final String value) throws InvalidException {
        return switch (value) {
            case GrammarTokens.EQ -> EQ;
            case GrammarTokens.NEQ -> NEQ;
            case GrammarTokens.GT -> GT;
            case GrammarTokens.GEQ -> GEQ;
            case GrammarTokens.LT -> LT;
            case GrammarTokens.LEQ -> LEQ;
            default -> throw new InvalidException(value);
        };
    }

    /**
     * Converts an {@code ComparisonOperator} enum to string format
     *
     * @param cmpOperator operator enum
     * @return operator as string
     */
    public static String toString(final ComparisonOperator cmpOperator) {
        return switch (cmpOperator) {
            case EQ -> GrammarTokens.EQ;
            case NEQ -> GrammarTokens.NEQ;
            case GT -> GrammarTokens.GT;
            case GEQ -> GrammarTokens.GEQ;
            case LT -> GrammarTokens.LT;
            case LEQ -> GrammarTokens.LEQ;
        };
    }

    public static String toCode(final ComparisonOperator cmpOperator) {
        return cmpOperator == EQ ? ComparisonOperator.EQ_CODE : toString(cmpOperator);
    }

    public static final class InvalidException extends Exception {
        private static final String MESSAGE = "Invalid ComparisonOperator <%s>";
        public InvalidException(final String value) {
            super(MESSAGE.formatted(value));
        }
    }
}
