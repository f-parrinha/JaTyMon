package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UndefinedStateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Undefined state '%s'.";
    public UndefinedStateDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String stateName) {
        super(String.format(MESSAGE, stateName), protocolName, tokenPosition);
    }
}
