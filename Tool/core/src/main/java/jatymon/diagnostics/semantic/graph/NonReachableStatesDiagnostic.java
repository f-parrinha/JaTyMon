package jatymon.diagnostics.semantic.graph;

import jatymon.diagnostics.semantic.SemanticErrorDiagnostic;
import jatymon.common.TokenPosition;
import jatymon.typestate.graph.nodes.GraphNode;

import java.util.Set;

public class NonReachableStatesDiagnostic extends SemanticErrorDiagnostic {
    public static final String MESSAGE = "The protocol contains non-reachable states: %s.";
    public NonReachableStatesDiagnostic(final String protoName, final Set<GraphNode> nonReachableNodes) {
        super(String.format(MESSAGE, nonReachableNodes), protoName, TokenPosition.NIL);
    }
}
