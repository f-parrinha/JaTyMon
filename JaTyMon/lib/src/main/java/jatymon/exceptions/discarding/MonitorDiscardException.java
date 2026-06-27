package jatymon.exceptions.discarding;

/**
 * Interface {@code AlertException} represents a type of exceptions that are used to communicate with the monitors. They stop
 *  monitoring execution, but are not considered internal exceptions and as such are not logged by default.
 */
public abstract class MonitorDiscardException extends RuntimeException {
    public static final String NAME = MonitorDiscardException.class.getSimpleName();

    public MonitorDiscardException(final String message) {
        super(message);
    }

    public MonitorDiscardException() {}
}
