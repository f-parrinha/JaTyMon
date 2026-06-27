package jatymon.exceptions;

public class MonitorWriteFailedException extends RuntimeException {
    public static final String MESSAGE = "Failed to write monitor at '%s'";
    public MonitorWriteFailedException(String path) {
        super(String.format(MESSAGE, path));
    }
}

