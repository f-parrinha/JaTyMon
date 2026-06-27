package jatymon.diagnostics;

import javax.tools.Diagnostic;

/**
 * Instances of the class {@code NoteDiagnostic} are ERROR logs of the Typestate and Monitor generation processor
 * @author Francisco Parrinha
 */
public class ErrorDiagnostic extends AbstractDiagnostic {
    public ErrorDiagnostic(final String message) {
        super(Diagnostic.Kind.ERROR, message);
    }
}
