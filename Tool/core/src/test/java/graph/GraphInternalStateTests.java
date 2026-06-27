package graph;

import common.Tests;
import jatymon.actions.ActionType;
import jatymon.typestate.graph.GraphFactory;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.ratios.Ratio;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GraphInternalStateTests extends GraphTester {

    @Test
    public void testABPDual() throws IOException {
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode state2 = new GraphNode("State2");

        // Create graph
        state1.addOutput(new GraphSingleTransition.Builder("conn", state1, state1, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state1.addOutput(new GraphSingleTransition.Builder("connUp", state1, state2, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .withPreAssignments(Set.of("A1"))
                .withPredicates(Set.of("P1"))
                .build());
        state1.addOutput(new GraphSingleTransition.Builder("connFailed", state1, state1, ActionType.Input)
                .withRatio(new Ratio(0))
                .withPreAssignments(Set.of("A2"))
                .build());
        state2.addOutput(new GraphSingleTransition.Builder("msg", state2, state2, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state2.addOutput(new GraphSingleTransition.Builder("ack", state2, state2, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state2.addOutput(new GraphSingleTransition.Builder("connFailed", state2, state1, ActionType.Input)
                .withRatio(new Ratio(0))
                .withPreAssignments(Set.of("A2"))
                .build());

        // Test
        graphEquals(List.of(Tests.InternalState.ABP_DUAL), List.of(GraphFactory.fromGraphStartNode("ABPDual", state1)));
    }
}
