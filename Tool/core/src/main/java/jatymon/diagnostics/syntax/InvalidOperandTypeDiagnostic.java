package jatymon.diagnostics.syntax;

import jatymon.common.TokenPosition;

public class InvalidOperandTypeDiagnostic extends SyntaxErrorDiagnostic {
    public static final String MESSAGE = "Invalid operand type was given. Operand must either be a name or a number.";

    public InvalidOperandTypeDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(fileName, tokenPosition, MESSAGE);
    }
}
