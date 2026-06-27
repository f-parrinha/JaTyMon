package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class ExtFieldNotFoundInClassDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Ext field '%s' not found in class '%s'";
    public ExtFieldNotFoundInClassDiagnostic(final String extName,
                                             final String className,
                                             final String fileName,
                                             final TokenPosition tokenPosition) {
        super(MESSAGE.formatted(extName, className), fileName, tokenPosition);
    }
}
