package jatymon.logging.logs.internal;

import jatymon.logging.logs.ErrorLog;

public class LoggerWriteFailedLog extends ErrorLog {
    public static final String MESSAGE = "Failed to write logs to a file";
    private static final String TITLE = "INTERNAL";

    public LoggerWriteFailedLog() {
        super(TITLE, MESSAGE);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof LoggerWriteFailedLog log && super.equals(log);
    }
}
