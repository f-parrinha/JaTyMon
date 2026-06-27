package jatymon.diagnostics.semantic.resolving;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticWarningDiagnostic;

public class UnusedImportDiagnostic extends SemanticWarningDiagnostic {
    public static final String MESSAGE = "Unused import '%s'";
    public UnusedImportDiagnostic(final String imp, final String fileName) {
        super(MESSAGE.formatted(imp), fileName, TokenPosition.NIL);
    }
}
