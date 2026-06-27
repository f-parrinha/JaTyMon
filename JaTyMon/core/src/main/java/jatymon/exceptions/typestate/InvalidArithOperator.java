package jatymon.exceptions.typestate;

public class InvalidArithOperator extends RuntimeException {
    public static final String MESSAGE = "Invalid arith operator '%s'.";
    public InvalidArithOperator(String operator) {
        super(String.format(MESSAGE, operator));
    }
}
