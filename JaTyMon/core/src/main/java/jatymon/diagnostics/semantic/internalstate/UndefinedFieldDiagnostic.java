package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UndefinedFieldDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unknown field with name '%s'.";
    public UndefinedFieldDiagnostic(final String name, final String fileName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
