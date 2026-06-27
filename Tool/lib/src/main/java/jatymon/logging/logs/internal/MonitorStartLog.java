package jatymon.logging.logs.internal;

import jatymon.logging.logs.NoteLog;

public class MonitorStartLog extends NoteLog {
    public static final String MESSAGE = "Starting monitor with confidence level %f";

    public MonitorStartLog(final String title, final double confidenceLevel) {
        super(title, MESSAGE.formatted(confidenceLevel));
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MonitorStartLog log && super.equals(log);
    }
}
