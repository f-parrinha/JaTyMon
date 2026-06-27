package jatymon.exceptions;

import jatymon.actions.ActionId;

public class InvalidSampleIdException extends RuntimeException {
    public static final String MESSAGE = "The invalid monitor action <%s>.";
    public InvalidSampleIdException(final ActionId action) {
        super(String.format(MESSAGE, action));
    }
}
