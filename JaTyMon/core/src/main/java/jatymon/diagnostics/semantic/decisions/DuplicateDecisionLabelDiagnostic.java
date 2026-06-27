package jatymon.diagnostics.semantic.decisions;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateDecisionLabelDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate label '%s'.";
    public DuplicateDecisionLabelDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String label) {
        super(String.format(MESSAGE, label), protocolName, tokenPosition);
    }
}
