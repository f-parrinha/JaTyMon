package jatymon.diagnostics.syntax;

import jatymon.common.TokenPosition;

public class InvalidBoolExprSizeDiagnostic extends SyntaxErrorDiagnostic {
    public static final String MESSAGE = "Invalid boolean expression size '%s'";

    public InvalidBoolExprSizeDiagnostic(final String fileName, final TokenPosition tokenPosition, final int size) {
        super(fileName, tokenPosition, String.format(MESSAGE, size));
    }
}
