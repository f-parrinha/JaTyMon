package jatymon.diagnostics.syntax;

import jatymon.diagnostics.ErrorDiagnostic;
import jatymon.common.TokenPosition;

/**
 * Class {@code SyntaxErrorDiagnostic} represents a syntax error found during parsing
 * @author Francisco Parrinha
 */
public class SyntaxErrorDiagnostic extends ErrorDiagnostic {
    public static final String MESSAGE = "[%s %s] %s";
    public SyntaxErrorDiagnostic(final String fileName, final TokenPosition tokenPosition, final String message) {
        super(String.format(MESSAGE, fileName, tokenPosition, message));
    }
}
