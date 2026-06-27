package graph;

import common.Tests;
import jatymon.actions.ActionType;
import jatymon.typestate.graph.GraphFactory;
import jatymon.typestate.graph.nodes.GraphEndNode;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GraphAnonymousTests extends GraphTester {

    @Test
    public void testAnonymousSimple() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode state2 = new GraphNode("State2");
        final GraphNode anon = new GraphNode("anonymous::4:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, anon, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, state2, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m1", state1, state0, ActionType.Input));
        state2.addOutput(new GraphSingleTransition("m2", state2, end, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m1", anon, state1, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m2", anon, state2, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_SIMPLE), List.of(GraphFactory.fromGraphStartNode("AnonymousSimple", state0)));
    }

    @Test
    public void testAnonymousComplex() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode state2 = new GraphNode("State2");
        final GraphNode anon1 = new GraphNode("anonymous::3:19");
        final GraphNode anon2 = new GraphNode("anonymous::12:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, anon1, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, state2, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m1", state1, state0, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m2", state1, anon2, ActionType.Input));
        state2.addOutput(new GraphSingleTransition("m2", state2, end, ActionType.Input));
        anon1.addOutput(new GraphSingleTransition("m1", anon1, state1, ActionType.Input));
        anon1.addOutput(new GraphSingleTransition("m2", anon1, state2, ActionType.Input));
        anon2.addOutput(new GraphSingleTransition("m1", anon2, state1, ActionType.Input));
        anon2.addOutput(new GraphSingleTransition("m2", anon2, state2, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_COMPLEX), List.of(GraphFactory.fromGraphStartNode("AnonymousComplex", state0)));
    }

    @Test
    public void testAnonymousEnd() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon = new GraphNode("anonymous::3:18");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m", state0, anon, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m", anon, end, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_END), List.of(GraphFactory.fromGraphStartNode("AnonymousEnd", state0)));
    }

    @Test
    public void testAnonymousEnd2() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon = new GraphNode("anonymous::4:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, end, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, anon, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m1", anon, state0, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_END2), List.of(GraphFactory.fromGraphStartNode("AnonymousEnd2", state0)));
    }

    @Test
    public void testAnonymousEnd3() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon = new GraphNode("anonymous::4:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, end, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, anon, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m1", anon, state0, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m2", anon, end, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_END3), List.of(GraphFactory.fromGraphStartNode("AnonymousEnd3", state0)));
    }

    @Test
    public void testAnonymousNotEnd() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon = new GraphNode("anonymous::3:18");

        state0.addOutput(new GraphSingleTransition("m", state0, anon, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m", anon, state0, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_NOT_END), List.of(GraphFactory.fromGraphStartNode("AnonymousNotEnd", state0)));
    }

    @Test
    public void testAnonymousNotEnd2() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode anon = new GraphNode("anonymous::4:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, end, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, anon, ActionType.Input));
        anon.addOutput(new GraphSingleTransition("m1", anon, state1, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m1", state1, state1, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_NOT_END2), List.of(GraphFactory.fromGraphStartNode("AnonymousNotEnd2", state0)));
    }

    @Test
    public void testAnonymousIdentity() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon1 = new GraphNode("anonymous::4:19");
        final GraphNode anon2 = new GraphNode("anonymous::7:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m", state0, end, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m1", state0, anon1, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, anon2, ActionType.Input));
        anon1.addOutput(new GraphSingleTransition("m", anon1, end, ActionType.Input));
        anon2.addOutput(new GraphSingleTransition("m", anon2, end, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_IDENTITY), List.of(GraphFactory.fromGraphStartNode("AnonymousIdentity", state0)));
    }

    @Test
    public void testAnonymousIdentity2() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode anon1 = new GraphNode("anonymous::4:19");
        final GraphNode anon2 = new GraphNode("anonymous::7:19");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m", state0, end, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m1", state0, anon1, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("m2", state0, anon2, ActionType.Input));
        anon1.addOutput(new GraphSingleTransition("m", anon1, end, ActionType.Input));
        anon2.addOutput(new GraphSingleTransition("m1", anon2, end, ActionType.Input));
        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_IDENTITY2), List.of(GraphFactory.fromGraphStartNode("AnonymousIdentity2", state0)));
    }

    @Test
    public void testAnonymousWithDecisionState() throws IOException  {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphNode anon1 = new GraphNode("anonymous::6:19");
        final GraphNode anon2 = new GraphNode("anonymous::9:32");
        final GraphNode anon3 = new GraphNode("anonymous::10:69");
        final GraphNode anon4 = new GraphNode("anonymous::11:80");
        final GraphEndNode end = new GraphEndNode();

        state0.addOutput(new GraphSingleTransition("m1", state0, anon1, ActionType.Input));
        state0.addOutput(new GraphMultiTransition("m2", state0, Set.of(
                new GraphMultiTransition.Decision("TEST_1", anon2),
                new GraphMultiTransition.Decision("TEST_2", state0),
                new GraphMultiTransition.Decision("TEST_3", end)), ActionType.Input));
        anon1.addOutput(new GraphSingleTransition("m1", anon1, state0, ActionType.Input));
        anon2.addOutput(new GraphMultiTransition("m2", anon2, Set.of(
                new GraphMultiTransition.Decision("TEST_1", state0),
                new GraphMultiTransition.Decision("TEST_2", end),
                new GraphMultiTransition.Decision("TEST_3", anon3)), ActionType.Input));
        anon3.addOutput(new GraphMultiTransition("m2", anon3, Set.of(
                new GraphMultiTransition.Decision("TEST_1", state1),
                new GraphMultiTransition.Decision("TEST_2", state1),
                new GraphMultiTransition.Decision("TEST_3", anon4)), ActionType.Input));
        anon4.addOutput(new GraphSingleTransition("m1", anon4, state1, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("m1", state1, state1, ActionType.Input));

        graphEquals(List.of(Tests.Anonymous.ANONYMOUS_WITH_DECISION_STATE), List.of(GraphFactory.fromGraphStartNode("AnonymousWithDecisionState", state0)));
    }
}
