package jatymon.diagnostics.syntax;

import jatymon.common.TokenPosition;

public class InvalidArithExprSizeDiagnostic extends SyntaxErrorDiagnostic {
    public static final String MESSAGE = "Invalid arithmetic expression size '%s'";
    public InvalidArithExprSizeDiagnostic(final String fileName, final TokenPosition tokenPos, final int size) {
        super(fileName, tokenPos, String.format(MESSAGE, size));
    }
}
