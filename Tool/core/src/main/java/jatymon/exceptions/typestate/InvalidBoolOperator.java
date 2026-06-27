package jatymon.exceptions.typestate;

public class InvalidBoolOperator extends RuntimeException {
    public static final String MESSAGE = "Invalid boolean operator '%s'";
    public InvalidBoolOperator(String operator) {
        super(String.format(MESSAGE, operator));
    }
}
