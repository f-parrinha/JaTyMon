package jatymon.diagnostics.semantic.graph;

import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;
import jatymon.common.TokenPosition;
import jatymon.typestate.graph.nodes.GraphNode;

import java.util.Set;

public class NonProductiveStatesDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "The protocol contains non-productive states: %s.";
    public NonProductiveStatesDiagnostic(final String protocolName, final Set<GraphNode> nonProductiveStates) {
        super(String.format(MESSAGE, nonProductiveStates), protocolName, TokenPosition.NIL);
    }
}
