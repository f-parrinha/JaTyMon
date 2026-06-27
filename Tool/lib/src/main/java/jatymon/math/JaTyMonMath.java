package jatymon.math;

import jatymon.math.intervals.BoundedInterval;
import jatymon.math.intervals.Interval;

/**
 * Class {@code RatiosMathUtils} contains utility methods used during ratio calculation and evaluation
 * @author Francisco Parrinha
 */
public class JaTyMonMath {

    /* Z-Score coefficients from Abramowitz & Stegun (1964), formula 26.2.23 */
    private static final double C0 = 2.515517;
    private static final double C1 = 0.802853;
    private static final double C2 = 0.010328;
    private static final double D1 = 1.432788;
    private static final double D2 = 0.189269;
    private static final double D3 = 0.001308;


    /**
     * Calculates the standard error SE of a given sample id {@code (state, method)}
     * <p>
     *     PRE: The true ratio must belong to a method in the same state to which the samples count references to
     * </p>
     * @param trueRatio reference true/assigned/expected ratio to a method {@code m} in a state {@code s}
     * @param stateActionsCount number of action executions in state {@code s}
     * @return standard error
     */
    public static double getStandardError(final double trueRatio, final int stateActionsCount) {
        return Math.sqrt((trueRatio * (1 - trueRatio))/ stateActionsCount);
    }

    /**
     * Calculates a confidence interval around the true ratio for a given sample id
     * <p>
     *     PRE: the given standard error
     * </p>
     * @param confidenceLevel reference confidence level
     * @param standardError reference standard error
     * @param trueRatio reference true/assigned/expected ratio
     * @return confidence interval around true ratio
     */
    public static Interval getConfidenceInterval(final double confidenceLevel, final double standardError, final double trueRatio) {
        final double maxAcceptableError = zscore(confidenceLevel) * standardError;
        return new BoundedInterval(trueRatio - maxAcceptableError, trueRatio + maxAcceptableError);
    }

    /**
     * Returns the Z critical value for a given confidence level.
     * From Abramowitz & Stegun (1964), formula 26.2.23
     * <p>
     *     PRE: the confidence level is in [0;1[
     * </p>
     * @param confidenceLevel the desired confidence level
     * @return z-score value (x where Q(x) = p, with 0 < p <= 0.5)
     */
    public static double zscore(final double confidenceLevel) {
        double p = (1 - confidenceLevel) / 2.0;

        // Math.sqrt(Math.log(1.0 / (p * p))) = Math.sqrt(Math.log(p^-2)) = Math.sqrt(-2 * Math.log(p))
        double t = Math.sqrt(-2.0 * Math.log(p));
        double numerator = C0 + C1 * t + C2 * t * t;
        double denominator = 1 + D1 * t + D2 * t * t + D3 * t * t * t;
        return t - (numerator / denominator);
    }
}
