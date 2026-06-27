package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class ExtFieldsAreImmutableDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Cannot change the value of an Ext fields. Ext fields are immutable.";
    public ExtFieldsAreImmutableDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(MESSAGE, fileName, tokenPosition);
    }
}
