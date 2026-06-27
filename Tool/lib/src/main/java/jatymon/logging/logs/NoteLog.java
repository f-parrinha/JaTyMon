package jatymon.logging.logs;

import javax.tools.Diagnostic;

public class NoteLog extends AbstractLog {
    public static final Diagnostic.Kind KIND = Diagnostic.Kind.NOTE;

    public NoteLog(final String title, final String message) {
        super(title, message);
    }

    @Override
    public Diagnostic.Kind getKind() {
        return KIND;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof NoteLog log && super.equals(log);
    }
}
