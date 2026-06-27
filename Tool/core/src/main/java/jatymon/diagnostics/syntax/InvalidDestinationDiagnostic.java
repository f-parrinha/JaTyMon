package jatymon.diagnostics.syntax;

import jatymon.common.TokenPosition;

public class InvalidDestinationDiagnostic extends SyntaxErrorDiagnostic {
    public static final String MESSAGE = "Invalid destination. Must be either ID, State, or Decision State";
    public InvalidDestinationDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(fileName, tokenPosition, MESSAGE);
    }
}
