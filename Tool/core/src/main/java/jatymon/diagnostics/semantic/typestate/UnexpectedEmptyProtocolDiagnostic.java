package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UnexpectedEmptyProtocolDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Expected at least one defined state.";
    public UnexpectedEmptyProtocolDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE), fileName, tokenPosition);
    }
}
