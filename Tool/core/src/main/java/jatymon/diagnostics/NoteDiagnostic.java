package jatymon.diagnostics;

import javax.tools.Diagnostic;

/**
 * Instances of the class {@code NoteDiagnostic} are INFO logs of the Typestate and Monitor generation processor
 * @author Francisco Parrinha
 */
public class NoteDiagnostic extends AbstractDiagnostic {
    public NoteDiagnostic(String message) {
        super(Diagnostic.Kind.NOTE, message);
    }
}
