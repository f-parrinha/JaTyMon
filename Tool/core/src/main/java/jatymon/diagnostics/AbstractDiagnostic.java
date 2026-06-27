package jatymon.diagnostics;

import javax.tools.Diagnostic;

/**
 * Instances of the class {@code AbstractDiagnostic} are logs of the Typestate and Monitor generation processor
 * @author Francisco Parrinha
 */
public class AbstractDiagnostic {
    private final Diagnostic.Kind kind;
    private final String message;

    public AbstractDiagnostic(Diagnostic.Kind kind, String message) {
        this.kind = kind;
        this.message = message;
    }


    public Diagnostic.Kind getKind() {
        return kind;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
