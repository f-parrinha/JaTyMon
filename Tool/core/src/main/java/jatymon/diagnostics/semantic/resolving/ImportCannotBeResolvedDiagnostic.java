package jatymon.diagnostics.semantic.resolving;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class ImportCannotBeResolvedDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Import '%s' cannot be resolved.";
    public ImportCannotBeResolvedDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String qualifiedName) {
        super(String.format(MESSAGE, qualifiedName), protocolName, tokenPosition);
    }
}
