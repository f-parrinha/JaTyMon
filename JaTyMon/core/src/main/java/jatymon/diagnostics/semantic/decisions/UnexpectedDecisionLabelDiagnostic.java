package jatymon.diagnostics.semantic.decisions;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UnexpectedDecisionLabelDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unexpected decision label '%s'.";
    public UnexpectedDecisionLabelDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String label) {
        super(String.format(MESSAGE, label), protocolName, tokenPosition);
    }
}
