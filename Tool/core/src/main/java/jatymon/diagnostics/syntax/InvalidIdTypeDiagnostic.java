package jatymon.diagnostics.syntax;

import jatymon.common.TokenPosition;

public class InvalidIdTypeDiagnostic extends SyntaxErrorDiagnostic {
    public static final String MESSAGE = "Invalid ID type";
    public InvalidIdTypeDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(fileName, tokenPosition, MESSAGE);
    }
}
