package jatymon.math.intervals;

/**
 * Tuple representing a top bounded interval
 * @author Francisco Parrinha
 * @param max higher border
 */
public record TopBoundedInterval(double max) implements Interval {
    public boolean contains(final double val) {
        return max <= val;
    }

    @Override
    public boolean ratioIsGreater(double ratio) {
        return ratio > max;
    }

    @Override
    public boolean ratioIsSmaller(double ratio) {
        return false;
    }

    @Override
    public double min() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public String toString() {
        return "]-infinity; %s]".formatted(max);
    }
}
