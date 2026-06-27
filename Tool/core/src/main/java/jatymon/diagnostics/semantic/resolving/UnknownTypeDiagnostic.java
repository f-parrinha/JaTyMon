package jatymon.diagnostics.semantic.resolving;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UnknownTypeDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unknown type '%s'.";
    public UnknownTypeDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String refName) {
        super(String.format(MESSAGE, refName), protocolName, tokenPosition);
    }
}
