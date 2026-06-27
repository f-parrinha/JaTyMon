package jatymon.exceptions.graph;

public class UnknownTransitionTypeException extends RuntimeException {
    public static final String MESSAGE = "Unknown transition type <%s>.";
    public UnknownTransitionTypeException(Class<?> clazz) {
        super(MESSAGE.formatted(clazz));
    }
}
