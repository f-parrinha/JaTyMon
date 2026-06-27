package jatymon.logging.logs.internal;

import jatymon.logging.logs.ErrorLog;

public class InternalExceptionLog extends ErrorLog {
    public static final String MESSAGE = "Internal Exception -> %s";

    private final String messageBody;

    public InternalExceptionLog(final String title, final String messageBody) {
        super(title, MESSAGE.formatted(messageBody));
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof InternalExceptionLog log && super.equals(log) && log.messageBody.equals(messageBody);
    }
}
