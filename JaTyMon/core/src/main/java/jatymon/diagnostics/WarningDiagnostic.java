package jatymon.diagnostics;

import javax.tools.Diagnostic;

/**
 * Instances of the class {@code NoteDiagnostic} are WARNING logs of the Typestate and Monitor generation processor
 * @author Francisco Parrinha
 */
public class WarningDiagnostic extends AbstractDiagnostic {
    public WarningDiagnostic(String message) {
        super(Diagnostic.Kind.WARNING, message);
    }
}
