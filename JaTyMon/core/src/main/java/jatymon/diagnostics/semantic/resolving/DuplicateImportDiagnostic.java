package jatymon.diagnostics.semantic.resolving;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticWarningDiagnostic;

public class DuplicateImportDiagnostic extends SemanticWarningDiagnostic {
    public static final String MESSAGE = "Duplicated import statement '%s'.";

    public DuplicateImportDiagnostic(final String fileName, final TokenPosition tokenPosition, final String importQfName) {
        super(String.format(MESSAGE, importQfName), fileName, tokenPosition);
    }
}
