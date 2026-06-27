package jatymon.diagnostics.semantic;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.ErrorDiagnostic;

public class SemanticErrorDiagnostic extends ErrorDiagnostic implements SemanticDiagnostic {
    private final String fileName;
    private final TokenPosition tokenPosition;

    public SemanticErrorDiagnostic(final String message, final String fileName, TokenPosition tokenPosition) {
        super(String.format(SEMANTIC_DIAGNOSTIC_LOG, fileName, tokenPosition, message));
        this.fileName = fileName;
        this.tokenPosition = tokenPosition;
    }

    @Override
    public TokenPosition getTokenPosition() {
        return tokenPosition;
    }

    @Override
    public String fileName() {
        return fileName;
    }
}
