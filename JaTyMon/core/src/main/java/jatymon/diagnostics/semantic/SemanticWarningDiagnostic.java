package jatymon.diagnostics.semantic;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.WarningDiagnostic;

public class SemanticWarningDiagnostic extends WarningDiagnostic implements SemanticDiagnostic {
    private final String fileName;
    private final TokenPosition tokenPosition;

    public SemanticWarningDiagnostic(final String message, final String fileName, final TokenPosition tokenPosition) {
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
