package jatymon.diagnostics.semantic.decisions;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class ExpectedDecisionStateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Expected a decision state.";
    public ExpectedDecisionStateDiagnostic(final String protocolName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE), protocolName, tokenPosition);
    }
}
