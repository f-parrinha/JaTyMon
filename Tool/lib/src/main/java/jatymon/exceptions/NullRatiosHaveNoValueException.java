package jatymon.exceptions;

public class NullRatiosHaveNoValueException extends RuntimeException {
    public static final String MSG = "NullRatios do not have any value, thus cannot return any ratio value.";
    public NullRatiosHaveNoValueException() {
        super(MSG);
    }
}
