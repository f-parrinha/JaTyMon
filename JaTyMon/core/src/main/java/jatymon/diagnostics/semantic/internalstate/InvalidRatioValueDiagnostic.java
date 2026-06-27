package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class InvalidRatioValueDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Invalid ratio value '%f'.";
    public InvalidRatioValueDiagnostic(final String protocolName, final TokenPosition tokenPosition, final double ratioValue) {
        super(String.format(MESSAGE, ratioValue), protocolName, tokenPosition);
    }
}
