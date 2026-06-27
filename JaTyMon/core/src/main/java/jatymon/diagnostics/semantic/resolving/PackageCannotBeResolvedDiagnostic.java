package jatymon.diagnostics.semantic.resolving;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class PackageCannotBeResolvedDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Package '%s' cannot be resolved.";
    public PackageCannotBeResolvedDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String qualifiedName) {
        super(String.format(MESSAGE, qualifiedName), protocolName, tokenPosition);
    }
}
