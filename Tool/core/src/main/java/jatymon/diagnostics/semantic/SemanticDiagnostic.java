package jatymon.diagnostics.semantic;

import jatymon.common.TokenPosition;

/**
 * Interface {@code SemanticDiagnostic} defines the contract for diagnostics that operate over evaluation of text in a file,
 * mainly typestate protocol files.
 * @author Francisco Parrinha
 */
public interface SemanticDiagnostic {
    String SEMANTIC_DIAGNOSTIC_LOG = "[%s %s] %s";

    TokenPosition getTokenPosition();
    String fileName();
}
