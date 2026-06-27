package jatymon.diagnostics.semantic.decisions;

import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class EnumerateAllDecisionsDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "All decisions in decision states must be enumerated.";
    public EnumerateAllDecisionsDiagnostic(final String fileName, final TokenPosition tokenPosition) {
        super(String.format(MESSAGE), fileName, tokenPosition);
    }
}
