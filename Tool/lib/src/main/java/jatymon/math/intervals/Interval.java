package jatymon.math.intervals;

public interface Interval {
    /**
     * Returns whether the given value fits in the interval
     * @param value value to compare
     * @return true if value fits, false if not
     */
    boolean contains(double value);

    /**
     * Returns whether the given ratio is greater than the top boundary
     * @param ratio ratio to compare
     * @return ratio is greater
     */
    boolean ratioIsGreater(double ratio);

    /**
     * Returns whether the given ratio is smaller than the bottom boundary
     * @param ratio ratio to compare
     * @return ratio is smaller
     */
    boolean ratioIsSmaller(double ratio);

    /**
     * Returns the top boundary
     * @return top boundary
     */
    double max();

    /**
     * Returns the bottom boundary
     * @return bottom boundary
     */
    double min();
}
