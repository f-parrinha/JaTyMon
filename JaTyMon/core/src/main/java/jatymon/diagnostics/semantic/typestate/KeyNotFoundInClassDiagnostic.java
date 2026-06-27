package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class KeyNotFoundInClassDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Key '%s' not found in class '%s'";

    public KeyNotFoundInClassDiagnostic(final String keyName, final String className, final String fileName, final TokenPosition tokenPosition) {
        super(MESSAGE.formatted(keyName, className), fileName, tokenPosition);
    }
}
