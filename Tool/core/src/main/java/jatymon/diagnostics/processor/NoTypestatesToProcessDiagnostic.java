package jatymon.diagnostics.processor;

import jatymon.diagnostics.NoteDiagnostic;

public class NoTypestatesToProcessDiagnostic extends NoteDiagnostic {
    public static final String MESSAGE = "No elements annotated with @Typestate were found";
    public NoTypestatesToProcessDiagnostic() {
        super(MESSAGE);
    }
}
