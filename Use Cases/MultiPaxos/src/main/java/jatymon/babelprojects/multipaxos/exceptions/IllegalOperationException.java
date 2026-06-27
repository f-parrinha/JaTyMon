package jatymon.babelprojects.multipaxos.exceptions;

import jatymon.babelprojects.multipaxos.session.Operation;

public class IllegalOperationException extends RuntimeException {
    public static final String MESSAGE = "Illegal operation '%s'";
    public IllegalOperationException(final Operation op) {
        super(MESSAGE.formatted(op));
    }
}
