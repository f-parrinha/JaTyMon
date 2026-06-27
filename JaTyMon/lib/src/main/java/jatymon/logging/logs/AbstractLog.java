package jatymon.logging.logs;

import javax.tools.Diagnostic;
import java.time.LocalDateTime;

public abstract class AbstractLog {
    private static final String MESSAGE_FORMAT = "(%s | %s) [%s] %s";

    private final LocalDateTime timestamp;
    private final String message;
    private final String title;

    public AbstractLog(final String title, final String messageBody) {
        this.title = title;
        this.timestamp = LocalDateTime.now();
        this.message = MESSAGE_FORMAT.formatted(timestamp, title, getClass().getSimpleName(), messageBody);
    }

    public abstract Diagnostic.Kind getKind();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AbstractLog log &&
                log.title.equals(title) &&
                log.timestamp.equals(timestamp);
    }
}
