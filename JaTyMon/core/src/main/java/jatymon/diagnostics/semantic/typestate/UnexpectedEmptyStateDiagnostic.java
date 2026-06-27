package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UnexpectedEmptyStateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unexpected empty state '%s'.";
    public UnexpectedEmptyStateDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String stateName) {
        super(String.format(MESSAGE, stateName), protocolName, tokenPosition);
    }
}
