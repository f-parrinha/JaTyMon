package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class UndefinedPredicateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Unknown predicate with name '%s'.";
    public UndefinedPredicateDiagnostic(final String name, final String fileName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE, name), fileName, tokenPosition);
    }
}
