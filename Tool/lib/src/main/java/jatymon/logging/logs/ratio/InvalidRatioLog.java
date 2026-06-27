package jatymon.logging.logs.ratio;

import jatymon.math.intervals.Interval;
import jatymon.actions.ActionId;

class InvalidRatioLog extends RatioLog {
    public static final String MESSAGE = "Ratio at (%s -> %s) is out of bounds: %s not in %s";

    public InvalidRatioLog(final String typestateName,
                           final ActionId actionId,
                           final double ratio,
                           final Interval interval) {
        super(typestateName, actionId, ratio, interval, MESSAGE.formatted(actionId.stateName(),
                actionId.actionName(),
                ratio,
                interval));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof InvalidRatioLog log && super.equals(log);
    }
}
