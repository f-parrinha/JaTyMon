package jatymon.logging.logs;

import javax.tools.Diagnostic;

public class ErrorLog extends AbstractLog {
    public static final Diagnostic.Kind KIND = Diagnostic.Kind.ERROR;

    public ErrorLog(final String title, final String message) {
        super(title, message);
    }

    @Override
    public Diagnostic.Kind getKind() {
        return KIND;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ErrorLog log && super.equals(log);
    }
}
