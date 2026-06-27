package jatymon.babelprojects.multipaxos.exceptions;

import jatymon.babelprojects.multipaxos.messages.replica.ReplicaMessage;

public class IllegalMessageException extends RuntimeException {
    public static final String MESSAGE = "Illegal message '%s'";
    public IllegalMessageException(final ReplicaMessage message) {
        super(MESSAGE.formatted(message));
    }
}
