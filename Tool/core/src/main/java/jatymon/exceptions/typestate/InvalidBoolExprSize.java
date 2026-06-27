package jatymon.exceptions.typestate;

public class InvalidBoolExprSize extends RuntimeException {
    public static final String MESSAGE = "There cannot be more than two BoolExpr in the same line. Current: '%s'";
    public InvalidBoolExprSize(int current) {
        super(String.format(MESSAGE, current));
    }
}
