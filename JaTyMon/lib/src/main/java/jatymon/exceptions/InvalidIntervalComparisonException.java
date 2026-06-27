package jatymon.exceptions;

import jatymon.math.intervals.Interval;

public class InvalidIntervalComparisonException extends RuntimeException {
    public static final String MESSAGE = "Invalid ratio '%s' fit test on the interval '%s'";
    public InvalidIntervalComparisonException(final double ratio, final  Interval interval) {
        super(MESSAGE.formatted(ratio, interval));
    }
}
