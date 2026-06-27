package graph;

import common.Tests;
import jatymon.common.TokenPosition;
import jatymon.actions.ActionType;
import jatymon.typestate.ast.nodes.ref.TIdNode;
import jatymon.typestate.ast.nodes.action.TActionNode;
import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.typestate.graph.GraphFactory;
import jatymon.typestate.graph.nodes.GraphEndNode;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.ratios.Ratio;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GraphPerDiagnosticTests extends GraphTester {
    // TODO: Add the remaining examples

    @Test
    public void testDuplicateAssignment() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_ASSIGNMENT), List.of(GraphFactory.fromGraphStartNode("DuplicateAssignment", state0)));
    }

    @Test
    public void testDuplicateDecisionLabel() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        final Set<GraphMultiTransition.Decision> decisions = Set.of(
                new GraphMultiTransition.Decision("TEST_1", state0),
                new GraphMultiTransition.Decision("TEST_2", state0),
                new GraphMultiTransition.Decision("TEST_3", state0));
        state0.addOutput(new GraphMultiTransition("m", state0, decisions, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_DECISION_LABEL), List.of(GraphFactory.fromGraphStartNode("DuplicateDecisionLabel", state0)));
    }

    @Test
    public void testDuplicateField() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_FIELD), List.of(GraphFactory.fromGraphStartNode("DuplicateField", state0)));
    }

    @Test
    public void testDuplicateImport() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_IMPORT), List.of(GraphFactory.fromGraphStartNode("DuplicateImport", state0)));
    }

    @Test
    public void testDuplicateMethod() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state1, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m1", state1, state1, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_METHOD), List.of(GraphFactory.fromGraphStartNode("DuplicateMethod", state0)));
    }

    @Test
    public void testDuplicatePredicate() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_PREDICATE), List.of(GraphFactory.fromGraphStartNode("DuplicatePredicate", state0)));
    }

    @Test
    public void testDuplicateState() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.DUPLICATE_STATE),
                List.of(GraphFactory.fromGraphStartNode("DuplicateState", state0)));
    }

    @Test
    public void testEnumerateAllDecisions() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        final Set<GraphMultiTransition.Decision> decisions = Set.of(
                new GraphMultiTransition.Decision("TEST_1", state0),
                new GraphMultiTransition.Decision("TEST_2", state0));
        state0.addOutput(new GraphMultiTransition("m", state0, decisions, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.ENUMERATE_ALL_DECISIONS),
                List.of(GraphFactory.fromGraphStartNode("EnumerateAllDecisions", state0)));
    }

    @Test
    public void testExpectedDecisionState() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.EXPECTED_DECISION_STATE),
                List.of(GraphFactory.fromGraphStartNode("ExpectedDecisionState", state0)));
    }

    @Test
    public void testExpectedMethod() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m1", state0, state0, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, state0, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m3", state0, state0, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m4", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.EXPECTED_METHOD),
                List.of(GraphFactory.fromGraphStartNode("ExpectedMethod", state0)));
    }

    @Test
    public void testExtFieldsAreImmutable() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.EXT_FIELDS_ARE_IMMUTABLE),
                List.of(GraphFactory.fromGraphStartNode("ExtFieldsAreImmutable", state0)));
    }

    @Test
    public void testImportCannotBeResolved() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.IMPORT_CANNOT_BE_RESOLVED),
                List.of(GraphFactory.fromGraphStartNode("ImportCannotBeResolved", state0)));
    }

    @Test
    public void testInvalidRatioSum() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.5))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, state0, ActionType.Input)
                .withRatio(new Ratio(0.4))
                .build());
        state0.addOutput(new GraphSingleTransition("m3", state0, state0, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.INVALID_RATIO_SUM),
                List.of(GraphFactory.fromGraphStartNode("InvalidRatioSum", state0)));
    }

    @Test
    public void testInvalidRatioValue() throws IOException {
        final GraphNode state0 = new GraphNode("State0");

        // Create graph
        state0.addOutput(new GraphSingleTransition.Builder("m1", state0, state0, ActionType.Input)
                .withRatio(new Ratio(-1))
                .build());
        state0.addOutput(new GraphSingleTransition.Builder("m2", state0, state0, ActionType.Input)
                .withRatio(new Ratio(2))
                .build());

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.INVALID_RATIO_VALUE),
                List.of(GraphFactory.fromGraphStartNode("InvalidRatioValue", state0)));
    }

    @Test
    public void testNonProductiveStates() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode state2 = new GraphNode("State2");

        // Create graph
        state0.addOutput(new GraphSingleTransition("m", state0, state1, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m", state1, new GraphEndNode(), ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m2", state1, state2, ActionType.Input));
        state2.addOutput(new GraphSingleTransition("m", state2, state2, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.NON_PRODUCTIVE_STATES),
                List.of(GraphFactory.fromGraphStartNode("NonProductiveStates", state0)));
    }

    @Test
    public void testNonReachableStates() throws IOException {
        // This test must be built using an "AST"

        final TIdNode stateId0 = new TIdNode(TokenPosition.NIL, "State0");
        final TIdNode stateId1 = new TIdNode(TokenPosition.NIL, "State1");

        final TStateNode state0 = new TStateNode(TokenPosition.NIL, "State0",
                List.of(new TActionNode(TokenPosition.NIL, new TIdNode(TokenPosition.NIL, "void"), "m", List.of(), stateId0, ActionType.Input)),
                List.of());

        final TStateNode state1 = new TStateNode(TokenPosition.NIL, "State1",
                List.of(new TActionNode(TokenPosition.NIL, new TIdNode(TokenPosition.NIL, "void"), "m", List.of(), stateId1, ActionType.Input)),
                List.of());

        // Test
        graphEquals(List.of(Tests.PerDiagnostic.NON_REACHABLE_STATES),
                List.of(GraphFactory.fromAst("NonReachableStates", state0, Set.of(state0, state1))));
    }
}
