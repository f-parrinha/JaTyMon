package jatymon.logging.logs;

import javax.tools.Diagnostic;

public class WarningLog extends AbstractLog {
    public static final Diagnostic.Kind KIND = Diagnostic.Kind.WARNING;

    public WarningLog(final String title, final String message) {
        super(title, message);
    }

    @Override
    public Diagnostic.Kind getKind() {
        return KIND;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof WarningLog log && super.equals(log);
    }
}
