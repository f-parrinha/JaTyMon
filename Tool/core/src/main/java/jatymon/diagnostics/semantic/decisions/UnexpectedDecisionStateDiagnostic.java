package jatymon.diagnostics.semantic.decisions;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UnexpectedDecisionStateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unexpected decision state.";
    public UnexpectedDecisionStateDiagnostic(String protocolName, TokenPosition tokenPosition) {
        super(String.format(MESSAGE), protocolName, tokenPosition);
    }
}
