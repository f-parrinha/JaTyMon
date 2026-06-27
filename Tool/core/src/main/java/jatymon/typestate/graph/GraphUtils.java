package jatymon.typestate.graph;

import jatymon.common.ActionSignature;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;

import java.util.*;

public class GraphUtils {

    /**
     * Sorts all transitions by name. The same method may be used as a transition in different states. It maps the method name (or transition name)
     *  to all its occurrences in the different states.
     * @param graph reference graph
     * @return map of (transition name -> list of transitions)
     */
    public static Map<ActionSignature, Set<GraphTransition>> getTransitionsBySignature(final Graph graph) {
        final Map<ActionSignature, Set<GraphTransition>> transitionsBySignature =  new HashMap<>();
        for (final GraphNode current : graph.getNodes()) {
            for (final GraphTransition transition : current.getOutputs().values()) {
                transitionsBySignature.computeIfAbsent(transition.signature(), k -> new HashSet<>()).add(transition);
            }
        }
        return transitionsBySignature;
    }
}
