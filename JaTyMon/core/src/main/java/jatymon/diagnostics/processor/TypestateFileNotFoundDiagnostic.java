package jatymon.diagnostics.processor;

import jatymon.diagnostics.ErrorDiagnostic;

public class TypestateFileNotFoundDiagnostic extends ErrorDiagnostic {
    public static final String MESSAGE = "The given typestate file '%s' was not found.";
    public TypestateFileNotFoundDiagnostic(final String filePath ) {
        super(String.format(MESSAGE, filePath));
    }
}
