package jatymon.typestate.graph;

import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.diagnostics.semantic.graph.NonReachableStatesDiagnostic;
import jatymon.diagnostics.semantic.graph.NonProductiveStatesDiagnostic;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;

import java.util.*;

/**
 * Class {@code Graph} represents the underlying graph defined by a typestate. It is associated to a protocol, thus,
 *  its name is the same as it's corresponding protocol. The graph serves to validate whether the protocol can reach
 *  the {@code end} state (e.g. does not get stuck in a loop), and to correctly generate a monitor, using every state
 *  and every parameter in a transition to correctly write the future Java code.
 * @author Francisco Parrinha
 */
public class Graph {
    private final Map<String, GraphNode> graphMap;
    private final GraphNode start;
    private final String protoName;

    public Graph(final String protoName, final GraphNode startNode, final Map<String, GraphNode> graphMap) {
        this.protoName = protoName;
        this.graphMap = graphMap;
        this.start = startNode;
    }

    public String getProtoName() {
        return protoName;
    }

    public GraphNode getStart() {
        return start;
    }

    public GraphNode getNodeByName(String stateName) {
        return graphMap.get(stateName);
    }

    public Map<String, GraphNode> asMap() {
        return Map.copyOf(graphMap);
    }

    public Set<String> getStateNames() {
        return Set.copyOf(graphMap.keySet());
    }

    public Set<GraphNode> getNodes() {
        return Set.copyOf(graphMap.values());
    }

    public boolean isEmpty() {
        return graphMap.isEmpty() && start == null;
    }

    public boolean hasTerminalState() {
        return graphMap.containsKey(TStateNode.END_STATE);
    }

    /**
     * Checks whether the graph can reach the END state if it contains an END state. The graph may contain loops,
     *  yet these must be breakable, such that the protocol can be terminated.
     * @return list with diagnostics
     */
    public List<AbstractDiagnostic> validate() {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();

        ValidationResult validationResult;
        if (!(validationResult = isReachable()).isSuccessful) {
            diagnostics.add(new NonReachableStatesDiagnostic(protoName, validationResult.errorNodes));
        }
        if (!(validationResult = isProductive()).isSuccessful) {
            diagnostics.add(new NonProductiveStatesDiagnostic(protoName, validationResult.errorNodes));
        }
        return diagnostics;
    }

    /**
     * Checks if all nodes are reachable from the {@code start} state. The function returns an object that stores
     *  the status of the operation (successful or not) and a set of erroneous nodes, these are, those that are not reachable.
     * @return validation result
     */
    public ValidationResult isReachable() {
        final Stack<GraphNode> pending = new Stack<>();
        final Set<GraphNode> remaining = new HashSet<>(graphMap.values());
        pending.push(start);

        // Transverse the graph
        while(!pending.isEmpty()) {
            final GraphNode current = pending.pop();
            remaining.remove(current);

            // Add pending outputs
            for (final var transition : current.getOutputs().values()) {
                if (transition instanceof GraphSingleTransition singleTransition) {
                    final GraphNode destination = singleTransition.getOut();
                    if (remaining.contains(destination)) {
                        pending.add(destination);
                    }
                } else if (transition instanceof GraphMultiTransition multiTransition) {
                    final Collection<GraphNode> destinations = multiTransition.getOuts().values();
                    for (final var destination : destinations) {
                        if (remaining.contains(destination)) {
                            pending.add(destination);
                        }
                    }
                }
            }
        }
        return new ValidationResult(remaining.isEmpty(), remaining);
    }

    /**
     * Checks if all nodes are productive if there is an {@code end} state. The function returns an object that stores
     *  the status of the operation (successful or not) and a set of erroneous nodes, these are, those that are not productive.
     * @return validation result
     */
    private ValidationResult isProductive() {
        if (!graphMap.containsKey(TStateNode.END_STATE)) {
            return new ValidationResult(true, Set.of());
        }

        final Stack<GraphNode> pending = new Stack<>();
        final Set<GraphNode> remaining = new HashSet<>(graphMap.values());
        pending.add(graphMap.get(TStateNode.END_STATE));

        // Transverse the graph
        while (!pending.isEmpty()) {
            final GraphNode current = pending.pop();
            remaining.remove(current);
            for (final GraphTransition inputTransition : current.getInputs().values()) {
                final GraphNode input = inputTransition.getIn();
                if (remaining.contains(input)) {
                    pending.add(input);
                }
            }
        }
        return new ValidationResult(remaining.isEmpty(), remaining);
    }


    @Override
    public String toString() {
        return protoName;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Graph other &&
                other.protoName.equals(protoName) &&
                other.start.equals(start) &&
                other.graphMap.equals(graphMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protoName, start, graphMap);
    }


    /**
     * Class {@code ValidationResult} contains attributes used by the validation process. For example, the graph must have useful states,
     *  so there are two validation steps: the first is to check if all nodes are reachable from the {@code start}, the other
     *  is to check if all nodes are productive, if there is an {@code end} state.
     */
    public static final class ValidationResult {
        private final boolean isSuccessful;
        private final Set<GraphNode> errorNodes;

        public ValidationResult(final boolean isSuccessful, final Set<GraphNode> errorNodes) {
            this.isSuccessful = isSuccessful;
            this.errorNodes = Set.copyOf(errorNodes);
        }
    }
}
