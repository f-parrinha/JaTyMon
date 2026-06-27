package jatymon.logging.logs.ratio;

import jatymon.actions.ActionId;
import jatymon.math.intervals.Interval;

class InvalidHighRatioLog extends RatioLog {
    public static final String MESSAGE = "(%s -> %s) %s too many: %s not in %s";

    public InvalidHighRatioLog(final String typestateName,
                           final ActionId actionId,
                           final double ratio,
                           final Interval interval) {
        super(typestateName, actionId, ratio, interval, MESSAGE.formatted(actionId.stateName(), actionId.actionName(),
                RatioLog.getSendingReceivingText(actionId, false),
                ratio,
                interval));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof InvalidHighRatioLog log && super.equals(log);
    }
}
