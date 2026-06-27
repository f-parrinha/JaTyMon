package jatymon.babelprojects.abdquorum.exceptions;

import jatymon.babelprojects.abdquorum.operations.Operation;

public class IllegalOperationException extends RuntimeException {
    public static final String MESSAGE = "Illegal operation '%s'";
    public IllegalOperationException(final Operation op) {
        super(MESSAGE.formatted(op));
    }
}
