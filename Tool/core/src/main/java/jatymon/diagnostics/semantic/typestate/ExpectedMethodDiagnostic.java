package jatymon.diagnostics.semantic.typestate;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class ExpectedMethodDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Expected method '%s' to be declared in the associated class '%s'.";
    public ExpectedMethodDiagnostic(final String protocolName, final TokenPosition tokenPosition, final String methodName,
                                    final String classQualifiedName) {
        super(String.format(MESSAGE, methodName, classQualifiedName), protocolName, tokenPosition);
    }
}
