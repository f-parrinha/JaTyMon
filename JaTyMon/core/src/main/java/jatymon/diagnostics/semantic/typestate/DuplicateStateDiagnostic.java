package jatymon.diagnostics.semantic.typestate;

import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.common.TokenPosition;
import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;

public class DuplicateStateDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "Duplicate state '%s'.";
    public DuplicateStateDiagnostic(final String fileName, final TokenPosition tokenPosition, final TStateNode state) {
        super(String.format(MESSAGE, state.getName()), fileName, tokenPosition);
    }
}
