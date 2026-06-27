package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateMethodDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate method.";

    public DuplicateMethodDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(MESSAGE, fileName, tokenPosition);
    }
}
