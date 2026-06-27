package jatymon.logging.logs.ratio;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.logging.logs.WarningLog;
import jatymon.math.intervals.Interval;

public abstract class RatioLog extends WarningLog {
    private final double currentRatio;
    private final ActionId actionId;
    private final Interval interval;


    public RatioLog(final String typestateName,
                    final ActionId actionId,
                    final double currentRatio,
                    final Interval interval,
                    final String messageBody) {
        super(typestateName, messageBody);
        this.actionId = actionId;
        this.currentRatio = currentRatio;
        this.interval = interval;
    }


    public static String getSendingReceivingText(final ActionId actionId, final boolean capitalized) {
        final ActionType actionType = actionId.actionType();
        final String res =  actionType == ActionType.Input ? "Receiving" : actionType == ActionType.Output ? "Sending" : "";
        return capitalized ? res : res.toLowerCase();
    }

    public ActionId getActionId() {
        return actionId;
    }


    public double getCurrentRatio() {
        return currentRatio;
    }

    public Interval getInterval() {
        return interval;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof RatioLog log && super.equals(log) &&
                log.currentRatio == currentRatio &&
                log.interval.equals(interval) &&
                log.actionId.equals(actionId);
    }
}
