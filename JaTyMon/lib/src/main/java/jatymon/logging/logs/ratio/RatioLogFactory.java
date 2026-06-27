package jatymon.logging.logs.ratio;

import jatymon.actions.ActionId;
import jatymon.exceptions.InvalidIntervalComparisonException;
import jatymon.math.intervals.Interval;

public class RatioLogFactory {

    public static RatioLog buildLog(final String typestateName,
                                    final ActionId actionId,
                                    final double ratio,
                                    final Interval interval) {
        if (interval.ratioIsGreater(ratio)) {
            return new InvalidHighRatioLog(typestateName, actionId, ratio, interval);
        } else if (interval.ratioIsSmaller(ratio)) {
            return new InvalidLowRatioLog(typestateName, actionId, ratio, interval);
        }

        throw new InvalidIntervalComparisonException(ratio, interval);
    }
}
