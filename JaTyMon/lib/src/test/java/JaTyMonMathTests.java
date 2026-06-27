import jatymon.math.intervals.Interval;
import jatymon.math.JaTyMonMath;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests all custom math methods for ratio calculation. The most important one is the {@code zScore} method due to its
 *  approximation error. We use fuzz testing and generate several inputs and to compare the outputs from our custom solution
 *  to an Apache's advanced math library. Apache's library is considered to be accurate:
 *  {@code NormalDistributution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9}
 */
public class JaTyMonMathTests {

    // Error in the formula A&S 26.2.23 is max 4.5e-4
    private static final double ZSCORE_DELTA = 4.5e-4;
    private static final int FUZZ_TEST_AMOUNT = 100_000;
    private static final double MAX_CONFIDENCE_LEVEL = 0.999999;
    private static final String MAX_OBSERVED_ERROR_MESSAGE = "Max observed error %e exceeds A&S 26.2.23 bound of %e";
    private static final String ZSCORE_FUZZ_TEST_FAIL = "Z-Score fuzz test failed for confidence level=%s";
    private static final String SE_FUZZ_TEST_FAIL = "Standard error fuzz test failed for p=%s, n=%s";
    private static final String CI_FUZZ_TEST_FAIL = "Confidence interval fuzz test failed for p=%s, n=%s, confidence=%s";


    /* -------------- getStandardError() Tests -------------- */

    @Test
    void testGetStandardErrorReturnsZeroWhenTrueRatioIsZero() {
        assertEquals(0.0, JaTyMonMath.getStandardError(0.0, 100));
    }

    @Test
    void testGetStandardErrorReturnsZeroWhenTrueRatioIsOne() {
        assertEquals(0.0, JaTyMonMath.getStandardError(1.0, 100));
    }

    @Test
    void testGetStandardErrorIsMaximumAtFiftyPercent() {
        final double se = JaTyMonMath.getStandardError(0.5, 100);
        assertEquals(Math.sqrt(0.25 / 100), se);
    }

    @Test
    void testGetStandardErrorDecreasesAsSampleSizeGrows() {
        final double seSmall = JaTyMonMath.getStandardError(0.5, 100);
        final double seLarge = JaTyMonMath.getStandardError(0.5, 10_000);
        assertTrue(seLarge < seSmall);
    }

    @Test
    void testGetStandardErrorFuzz() {
        final Random random = new Random();

        /*
         * Kind of does not need this test because it's just the same formula, but it stays here in case someone touches
         *  the MathUtils.getStandardError() implementation
         */

        for (int i = 0; i < FUZZ_TEST_AMOUNT; i++) {
            final double p = random.nextDouble();
            final int n = random.nextInt(1_000_000) + 1;
            final double expected = Math.sqrt((p * (1 - p)) / n);
            final double actual = JaTyMonMath.getStandardError(p, n);
            assertEquals(expected, actual, SE_FUZZ_TEST_FAIL.formatted(p, n));
        }
    }


    /* -------------- getConfidenceInterval() Tests -------------- */

    @Test
    void testGetConfidenceIntervalContainsTrueRatio() {
        final double se = JaTyMonMath.getStandardError(0.5, 100);
        final Interval ci = JaTyMonMath.getConfidenceInterval(0.95, se, 0.5);
        assertTrue(ci.contains(0.5));
    }

    @Test
    void testGetConfidenceIntervalIsCenteredAroundTrueRatio() {
        final double se = JaTyMonMath.getStandardError(0.4, 200);
        final Interval ci = JaTyMonMath.getConfidenceInterval(0.95, se, 0.4);
        final double center = (ci.min() + ci.max()) / 2.0;
        assertEquals(0.4, center);
    }

    @Test
    void testGetConfidenceIntervalWidensWithHigherConfidence() {
        final double se = JaTyMonMath.getStandardError(0.5, 100);
        final Interval ci90 = JaTyMonMath.getConfidenceInterval(0.90, se, 0.5);
        final Interval ci99 = JaTyMonMath.getConfidenceInterval(0.99, se, 0.5);
        final double width90 = ci90.max() - ci90.min();
        final double width99 = ci99.max() - ci99.min();
        assertTrue(width99 > width90);
    }

    @Test
    void testGetConfidenceIntervalMinIsLessThanMax() {
        final double se = JaTyMonMath.getStandardError(0.5, 100);
        final Interval ci = JaTyMonMath.getConfidenceInterval(0.95, se, 0.5);
        assertTrue(ci.min() < ci.max());
    }

    @Test
    void testConfidenceIntervalFuzz() {
        final Random random = new Random();
        final NormalDistribution normal = new NormalDistribution();
        for (int i = 0; i < FUZZ_TEST_AMOUNT; i++) {
            final double p = random.nextDouble();
            final int n = random.nextInt(1_000_000) + 1;
            final double confidenceLevel = random.nextDouble() * MAX_CONFIDENCE_LEVEL;
            final double se = JaTyMonMath.getStandardError(p, n);
            final double z = normal.inverseCumulativeProbability((1 + confidenceLevel) / 2.0);

            final double expectedMin = p - (z * se);
            final double expectedMax = p + (z * se);

            final Interval actual = JaTyMonMath.getConfidenceInterval(confidenceLevel, se, p);

            // Scale by SE because our confidence interval calculation multiplies SE by zScore(l)
            final double ciDelta = se * ZSCORE_DELTA;
            final String errorMessage = CI_FUZZ_TEST_FAIL.formatted(p, n, confidenceLevel);
            assertEquals(expectedMin, actual.min(), ciDelta, errorMessage);
            assertEquals(expectedMax, actual.max(), ciDelta, errorMessage);
        }
    }

    /* -------------- zScore() Tests -------------- */

    @Test
    void testZScoreIsPositiveForAnyValidConfidenceLevel() {
        assertTrue(JaTyMonMath.zscore(0.80) > 0);
        assertTrue(JaTyMonMath.zscore(0.95) > 0);
        assertTrue(JaTyMonMath.zscore(0.99) > 0);
    }

    @Test
    void testZScoreIncreasesAsConfidenceLevelIncreases() {
        assertTrue(JaTyMonMath.zscore(0.90) < JaTyMonMath.zscore(0.95));
        assertTrue(JaTyMonMath.zscore(0.95) < JaTyMonMath.zscore(0.99));
    }

    @Test
    public void testZScoreFuzz() {
        final Random random = new Random();
        final NormalDistribution normal = new NormalDistribution();
        double maxObservedError = 0.0;
        for (int i = 0; i < FUZZ_TEST_AMOUNT; i++) {
            final double confidenceLevel = random.nextDouble() * MAX_CONFIDENCE_LEVEL;
            final double expected = normal.inverseCumulativeProbability((1 + confidenceLevel) / 2.0);
            final double actual = JaTyMonMath.zscore(confidenceLevel);
            final double error = Math.abs(expected - actual);
            maxObservedError = Math.max(maxObservedError, error);
            assertEquals(expected, actual, ZSCORE_DELTA, ZSCORE_FUZZ_TEST_FAIL.formatted(confidenceLevel));
        }

        // This is already tested in reality, because we use this delta in the assert equals above
        assertTrue(maxObservedError < ZSCORE_DELTA, MAX_OBSERVED_ERROR_MESSAGE.formatted(maxObservedError, ZSCORE_DELTA));
    }
}
