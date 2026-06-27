package jatymon.exceptions.typestate;

public class InvalidArithExprSize extends RuntimeException {
    public static final String MESSAGE = "There cannot be more than two ArithExpr in the same line. Current: '%s'";
    public InvalidArithExprSize(int current) {
        super(String.format(MESSAGE, current));
    }
}
