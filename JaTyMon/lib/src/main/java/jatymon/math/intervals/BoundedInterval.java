package jatymon.math.intervals;

/**
 * Tuple representing a bounded interval between two values (min, max)
 * @author Francisco Parrinha
 * @param min lower border
 * @param max higher border
 */
public record BoundedInterval(double min, double max) implements Interval {

    public boolean contains(final double val) {
        return min <= val && val <= max;
    }

    @Override
    public boolean ratioIsGreater(double ratio) {
        return ratio > max;
    }

    @Override
    public boolean ratioIsSmaller(double ratio) {
        return ratio < min;
    }

    @Override
    public String toString() {
        return "[%s; %s]".formatted(min, max);
    }
}
