package jatymon.typestate.graph;

import jatymon.common.Nameable;
import jatymon.actions.ActionType;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.ref.TIdNode;
import jatymon.typestate.ast.nodes.state.TDecisionNode;
import jatymon.typestate.ast.nodes.state.TDecisionStateNode;
import jatymon.typestate.ast.nodes.action.TActionNode;
import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.exceptions.graph.ExpectedDecisionStateDestinationException;
import jatymon.exceptions.graph.UnexpectedDecisionStateException;
import jatymon.exceptions.graph.UnknownDestinationTypeException;
import jatymon.exceptions.graph.UnknownTransitionTypeException;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;
import jatymon.ratios.Ratio;

import java.util.*;

/**
 * Class {@code GraphFactory} offers different methods of creating graphs for a typestate
 * @author Francisco Parrinha
 */
public class GraphFactory {

    /**
     * Stores relevant information for the construction of a transition by the factory class
     * @author Francisco Parrinha
     */
    private record TransitionData(
            Map<String, GraphNode> graphMap,
            String name,
            GraphNode inState,
            TNode destination,
            ActionType actionType,
            Ratio ratio,
            Set<String> preAssignments,
            Set<String> postAssignments,
            Set<String> predicates,
            String returnType,
            List<String> args) { /* Nothing to do here... */}

    /**
     * Creates a new Graph instance from a typestate AST data
     * @param protoName typestate name
     * @param startState first AST node state
     * @param stateNodes set of AST node states
     * @return new Graph instance
     */
    public static Graph fromAst(final String protoName, final TStateNode startState, final Set<TStateNode> stateNodes) {
        final Map<String, GraphNode> graphMap = new HashMap<>();
        for (final TStateNode stateNode : stateNodes) {
            final GraphNode currentNode = graphMap.computeIfAbsent(stateNode.getName(), GraphNode::new);
            final Set<String> seenMethods = new HashSet<>();
            for (final TActionNode action : stateNode.getActions()) {
                if (!seenMethods.add(action.getName())) continue;
                final TNode destination = action.getDestination();
                final TransitionData data = new TransitionData(
                        graphMap,
                        action.getName(),
                        currentNode,
                        destination,
                        action.getActionType(),
                        action.getRatio(),
                        Set.copyOf(action.getPreAssignmentNames()),
                        Set.copyOf(action.getPostAssignmentNames()),
                        Set.copyOf(action.getPredicateNames()),
                        action.getReturnType().getFullName(),
                        action.getArgsFullName());
                if ((destination instanceof TStateNode || destination instanceof TIdNode)) {
                    currentNode.addOutput(createSingleTransition(data));
                } else if (destination instanceof TDecisionStateNode) {
                    currentNode.addOutput(createMultiTransition(data));
                } else {
                    throw new UnknownDestinationTypeException();
                }
            }
        }
        return new Graph(protoName, graphMap.get(startState.getName()), graphMap);
    }

    /**
     * Creates a new Graph instance given the start node and the protocol name
     * @param protoName typestate name
     * @param startNode graph start node
     * @return new Graph instance
     */
    public static Graph fromGraphStartNode(final String protoName, final  GraphNode startNode) {
        final Map<String,GraphNode> graphMap = new HashMap<>();
        final Queue<GraphNode> pending = new LinkedList<>();
        pending.add(startNode);
        graphMap.put(startNode.getName(), startNode);

        while (!pending.isEmpty()) {
            final GraphNode current = pending.poll();
            for(final GraphTransition transition : current.getOutputs().values()) {
                if (transition instanceof GraphSingleTransition singleTransition) {
                    enqueueIfUnseen(singleTransition.getOut(), graphMap, pending);
                } else if (transition instanceof GraphMultiTransition multiTransition) {
                    final Collection<GraphNode> destinations = multiTransition.getOuts().values();
                    for (GraphNode destination : destinations) {
                        enqueueIfUnseen(destination, graphMap, pending);
                    }
                } else {
                    throw new UnknownTransitionTypeException(transition.getClass());
                }
            }
        }
        return new Graph(protoName, startNode, graphMap);
    }


    /*  ------------------- AUX METHODS ------------------- */


    private static GraphSingleTransition createSingleTransition(final TransitionData data) {
        final Map<String, GraphNode> graphMap = data.graphMap;
        final String destinationName = ((Nameable)  data.destination).getName();
        graphMap.computeIfAbsent(destinationName, GraphNode::new);
        return new GraphSingleTransition.Builder(data.name, data.inState, graphMap.get(destinationName), data.actionType)
                        .withRatio(data.ratio)
                        .withPreAssignments(data.preAssignments)
                        .withPostAssignments(data.postAssignments)
                        .withPredicates(data.predicates)
                        .withReturnType(data.returnType)
                        .withArgs(data.args)
                        .build();
    }

    private static GraphMultiTransition createMultiTransition(final TransitionData data) {
        if (!(data.destination instanceof TDecisionStateNode destination)) {
            throw new ExpectedDecisionStateDestinationException(data.destination);
        }

        final List<TDecisionNode> decisions = destination.getDecisions();
        final Set<GraphMultiTransition.Decision> graphDecisions = new HashSet<>();
        final Map<String, GraphNode> graphMap = data.graphMap;

        for (TDecisionNode decision : decisions) {
            final TNode decisionDestination = decision.getDestination();
            if (decisionDestination instanceof TDecisionStateNode) {
                throw new UnexpectedDecisionStateException();
            }
            if (decisionDestination instanceof TStateNode || decisionDestination instanceof TIdNode) {
                final String destinationName = ((Nameable) decisionDestination).getName();
                graphMap.computeIfAbsent(destinationName, GraphNode::new);
                graphDecisions.add(new GraphMultiTransition.Decision(decision.getLabel(), graphMap.get(destinationName)));
            }
        }

        return new GraphMultiTransition.Builder(data.name, data.inState, graphDecisions, data.actionType)
                .withRatio(data.ratio)
                .withPreAssignments(data.preAssignments)
                .withPostAssignments(data.postAssignments)
                .withPredicates(data.predicates)
                .withReturnType(data.returnType)
                .withArgs(data.args)
                .build();
    }

    private static void enqueueIfUnseen(final GraphNode node,
                                        final Map<String, GraphNode> graphMap,
                                        final Queue<GraphNode> pending) {
        if (!graphMap.containsKey(node.getName())) {
            graphMap.put(node.getName(), node);
            pending.add(node);
        }
    }
}
