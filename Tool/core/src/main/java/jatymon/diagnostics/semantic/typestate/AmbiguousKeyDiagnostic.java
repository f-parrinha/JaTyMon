package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class AmbiguousKeyDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Key '%s' is already declared as a field";

    public AmbiguousKeyDiagnostic(final String keyName, final String fileName, final TokenPosition tokenPosition) {
        super(MESSAGE.formatted(keyName), fileName, tokenPosition);
    }
}
