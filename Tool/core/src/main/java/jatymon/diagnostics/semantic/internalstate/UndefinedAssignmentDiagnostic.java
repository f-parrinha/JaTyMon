package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UndefinedAssignmentDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unknown assignment with name '%s'.";
    public UndefinedAssignmentDiagnostic(final String name, final String fileName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
