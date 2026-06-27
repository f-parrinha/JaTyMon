package jatymon.babelprojects.abdquorum.exceptions;

public class UnknownOperationException extends RuntimeException {
    public static final String MESSAGE = "Unknown operation with id '%s'";
    public UnknownOperationException(final String opId) {
        super(MESSAGE.formatted(opId));
    }
}
