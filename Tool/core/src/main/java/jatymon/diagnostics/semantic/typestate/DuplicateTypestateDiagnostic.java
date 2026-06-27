package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateTypestateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate typestate '%s'";
    public DuplicateTypestateDiagnostic(final String typestateQfName, final String fileName) {
        super(MESSAGE.formatted(typestateQfName), fileName, TokenPosition.NIL);
    }
}
