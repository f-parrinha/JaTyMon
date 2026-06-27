package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicatePredicateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate predicate name '%s'";
    public DuplicatePredicateDiagnostic(String name, String fileName, TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
