package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateAssignmentDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate assignment name '%s'";
    public DuplicateAssignmentDiagnostic(String name, String fileName, TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
