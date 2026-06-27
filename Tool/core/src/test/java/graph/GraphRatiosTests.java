package graph;

import common.Tests;
import jatymon.actions.ActionType;
import jatymon.typestate.graph.GraphFactory;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.typestate.graph.nodes.GraphEndNode;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class GraphRatiosTests extends GraphTester {

    @Test
    public void testRatioBadValue1() throws IOException {
        noProcessedGraphs(Tests.Ratios.RATIO_BAD_VALUE_1);
    }

    @Test
    public void testRatioBadValue2() throws IOException {
        noProcessedGraphs(Tests.Ratios.RATIO_BAD_VALUE_2);
    }

    @Test
    public void testRatioBadValue3() throws IOException {
        noProcessedGraphs(Tests.Ratios.RATIO_BAD_VALUE_3);
    }

    @Test
    public void testRatioExactBoundary() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m", state0, end, ActionType.Input)
                .withRatio(new Ratio(1))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_EXACT_BOUNDARY), List.of(GraphFactory.fromGraphStartNode("RatioExactBoundary", state0)));
    }

    @Test
    public void testRatioFloatingError() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.3333))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.3333))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m3", state0, end, ActionType.Input)
                .withRatio(new Ratio(0.3333))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_FLOATING_ERROR), List.of(GraphFactory.fromGraphStartNode("RatioFloatingError", state0)));
    }

    @Test
    public void testRatioNegative() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m", state0, end, ActionType.Input)
                .withRatio(new Ratio(-1))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_NEGATIVE), List.of(GraphFactory.fromGraphStartNode("RatioNegative", state0)));
    }

    @Test
    public void testRatioNegative2() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(-1))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(-2))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_NEGATIVE2), List.of(GraphFactory.fromGraphStartNode("RatioNegative2", state0)));
    }

    @Test
    public void testRatioNegativeAndPositive() throws IOException {
        noProcessedGraphs(Tests.Ratios.RATIO_NEGATIVE_AND_POSITIVE);
    }

    @Test
    public void testRatioNegativeAndPositive2() throws IOException {
        noProcessedGraphs(Tests.Ratios.RATIO_NEGATIVE_AND_POSITIVE2);
    }

    @Test
    public void testRatioOverFill() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.6))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.6))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m3", state0, end, ActionType.Input)
                .withRatio(new Ratio(0))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_OVER_FILL), List.of(GraphFactory.fromGraphStartNode("RatioOverFill", state0)));
    }

    @Test
    public void testRatioPositive() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m", state0, end, ActionType.Input)
                .withRatio(new Ratio(1))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_POSITIVE), List.of(GraphFactory.fromGraphStartNode("RatioPositive", state0)));
    }

    @Test
    public void testRatioPositive2() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m", state0, end, ActionType.Input)
                .withRatio(new Ratio(1.2))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_POSITIVE2), List.of(GraphFactory.fromGraphStartNode("RatioPositive2", state0)));
    }

    @Test
    public void testRatioPositive3() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.887654))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(0.112346))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_POSITIVE3), List.of(GraphFactory.fromGraphStartNode("RatioPositive3", state0)));
    }

    @Test
    public void testRatioSimple() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_SIMPLE), List.of(GraphFactory.fromGraphStartNode("RatioSimple", state0)));
    }

    @Test
    public void testRatioSumLessThan1() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(0.4))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_SUM_LESS_THAN_1), List.of(GraphFactory.fromGraphStartNode("RatioSumLessThan1", state0)));
    }

    @Test
    public void testRatioSumMoreThan1() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(0.6))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_SUM_MORE_THAN_1), List.of(GraphFactory.fromGraphStartNode("RatioSumMoreThan1", state0)));
    }

    @Test
    public void testRatioValueAndNone() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.2))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new NullRatio())
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m3", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.8))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_VALUE_AND_NONE), List.of(GraphFactory.fromGraphStartNode("RatioValueAndNone", state0)));
    }

    @Test
    public void testRatioZeroSum() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.0))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, end, ActionType.Input)
                .withRatio(new Ratio(0))
                .build());

        // Test
        graphEquals(List.of(Tests.Ratios.RATIO_ZERO_SUM), List.of(GraphFactory.fromGraphStartNode("RatioZeroSum", state0)));
    }
}
