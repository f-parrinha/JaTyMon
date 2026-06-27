package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class InvalidRatioSumDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Invalid ratio sum value '%f' in state '%s'.";
    public InvalidRatioSumDiagnostic(final double sum, final String stateName, final String protocolName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE, sum, stateName), protocolName, tokenPosition);
    }
}
