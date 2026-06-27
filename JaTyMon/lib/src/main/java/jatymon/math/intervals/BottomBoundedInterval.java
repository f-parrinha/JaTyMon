package jatymon.math.intervals;

/**
 * Tuple representing a bottom bounded interval
 * @author Francisco Parrinha
 * @param min lower border
 */
public record BottomBoundedInterval(double min) implements Interval {
    public boolean contains(final double val) {
        return min <= val;
    }

    @Override
    public boolean ratioIsGreater(double ratio) {
        return false;
    }

    @Override
    public boolean ratioIsSmaller(double ratio) {
        return ratio < min;
    }

    @Override
    public double max() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public String toString() {
        return "[%s; +infinity[".formatted(min);
    }
}
