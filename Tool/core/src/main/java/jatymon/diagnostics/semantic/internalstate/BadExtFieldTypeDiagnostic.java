package jatymon.diagnostics.semantic.internalstate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

import javax.lang.model.type.TypeKind;
import java.util.Set;

public class BadExtFieldTypeDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Ext field is of an incorrect type '%s'. Allowed types: '%s'";

    public BadExtFieldTypeDiagnostic(final TypeKind kind, final Set<TypeKind> allowedTypes, final String fileName, final TokenPosition tokenPosition) {
        super(MESSAGE.formatted(kind, allowedTypes), fileName, tokenPosition);
    }
}
