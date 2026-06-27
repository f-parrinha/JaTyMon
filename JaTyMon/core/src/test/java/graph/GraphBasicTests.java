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

public class GraphBasicTests extends GraphTester {

    @Test
    public void testCircular() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition("finish", state0, end, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.CIRCULAR), List.of(GraphFactory.fromGraphStartNode("Circular", state0)));
    }

    @Test
    public void testCircularWithGetter() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition("setF", state0, state0, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("finish", state0, end, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.CIRCULAR_WITH_GETTER), List.of(GraphFactory.fromGraphStartNode("CircularWithGetter", state0)));
    }

    @Test
    public void testFaultyFile() throws IOException {
        /*
         * The order to which this graph is created is different from the one written in the protocol file. This is done
         *  on purpose to test whether the graph equality comparison is order dependent.
         */

        final GraphNode init = new GraphNode("Init");
        final GraphNode open = new GraphNode("Open");
        final GraphNode read = new GraphNode("Read");
        final GraphNode close = new GraphNode("Close");
        final GraphNode anon = new GraphNode("anonymous::18:24");
        final GraphEndNode endNode = new GraphEndNode();

        // Create graph
        init.addOutput(new GraphMultiTransition("open", init, Set.of(
                new GraphMultiTransition.Decision("OK", open),
                new GraphMultiTransition.Decision("FOO", endNode)), ActionType.Input));
        open.addOutput(new GraphMultiTransition("hasNext", open, Set.of(
                new GraphMultiTransition.Decision("true", read)), ActionType.Input));
        open.addOutput(new GraphSingleTransition("hasNext2", open, anon, ActionType.Input));
        open.addOutput(new GraphSingleTransition("close", open, endNode, ActionType.Input));
        read.addOutput(new GraphMultiTransition("read", read, Set.of(
                new GraphMultiTransition.Decision("true", read),
                new GraphMultiTransition.Decision("false", close)), ActionType.Input));
        read.addOutput(new GraphSingleTransition("close", read, endNode, ActionType.Input));
        close.addOutput(new GraphSingleTransition("close", close, endNode, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.FAULTY_FILE), List.of(GraphFactory.fromGraphStartNode("FaultyFile", init)));
    }

    @Test
    public void testFaultyFile2() throws IOException {
        noProcessedGraphs(Tests.Basic.FAULTY_FILE2);
    }

    @Test
    public void testFile() throws IOException {
        final GraphNode init = new GraphNode("Init");
        final GraphNode open = new GraphNode("Open");
        final GraphNode read = new GraphNode("Read");
        final GraphNode close = new GraphNode("Close");
        final GraphEndNode endNode = new GraphEndNode();

        // Create graph
        init.addOutput(new GraphMultiTransition("open", init, Set.of(
                new GraphMultiTransition.Decision("OK", open),
                new GraphMultiTransition.Decision("ERROR", endNode)), ActionType.Input));
        open.addOutput(new GraphMultiTransition("hasNext", open, Set.of(
                new GraphMultiTransition.Decision("true", read),
                new GraphMultiTransition.Decision("false", close)), ActionType.Input));
        open.addOutput(new GraphSingleTransition("close", open, endNode, ActionType.Input));
        read.addOutput(new GraphSingleTransition("read", read, open, ActionType.Input));
        read.addOutput(new GraphSingleTransition("close", read, endNode, ActionType.Input));
        close.addOutput(new GraphSingleTransition("close", close, endNode, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.FILE), List.of(GraphFactory.fromGraphStartNode("File", init)));
    }

    @Test
    public void testFileInCollection() throws IOException {
        final GraphNode init = new GraphNode("Init");
        final GraphNode open = new GraphNode("Open");
        final GraphNode read = new GraphNode("Read");
        final GraphNode close = new GraphNode("Close");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        init.addOutput(new GraphMultiTransition("state", init, Set.of(
                new GraphMultiTransition.Decision("INIT", init),
                new GraphMultiTransition.Decision("OPEN", open),
                new GraphMultiTransition.Decision("READ", read),
                new GraphMultiTransition.Decision("CLOSE", close)), ActionType.Input));
        init.addOutput(new GraphMultiTransition("open", init, Set.of(
                new GraphMultiTransition.Decision("OK", open),
                new GraphMultiTransition.Decision("ERROR", end)), ActionType.Input));
        open.addOutput(new GraphMultiTransition("state", open, Set.of(
                new GraphMultiTransition.Decision("INIT", init),
                new GraphMultiTransition.Decision("OPEN", open),
                new GraphMultiTransition.Decision("READ", read),
                new GraphMultiTransition.Decision("CLOSE", close)), ActionType.Input));
        open.addOutput(new GraphMultiTransition("hasNext", open, Set.of(
                new GraphMultiTransition.Decision("true", read),
                new GraphMultiTransition.Decision("false", close)), ActionType.Input));
        open.addOutput(new GraphSingleTransition("close", open, end, ActionType.Input));
        read.addOutput(new GraphMultiTransition("state", read, Set.of(
                new GraphMultiTransition.Decision("INIT", init),
                new GraphMultiTransition.Decision("OPEN", open),
                new GraphMultiTransition.Decision("READ", read),
                new GraphMultiTransition.Decision("CLOSE", close)), ActionType.Input));
        read.addOutput(new GraphMultiTransition("hasNext", read, Set.of(
                new GraphMultiTransition.Decision("true", read),
                new GraphMultiTransition.Decision("false", close)), ActionType.Input));
        read.addOutput(new GraphSingleTransition("read", read, open, ActionType.Input));
        read.addOutput(new GraphSingleTransition("close", read, end, ActionType.Input));
        close.addOutput(new GraphMultiTransition("state", close, Set.of(
                new GraphMultiTransition.Decision("INIT", init),
                new GraphMultiTransition.Decision("OPEN", open),
                new GraphMultiTransition.Decision("READ", read),
                new GraphMultiTransition.Decision("CLOSE", close)), ActionType.Input));
        close.addOutput(new GraphSingleTransition("close", close, end, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.FILE_IN_COLLECTION), List.of(GraphFactory.fromGraphStartNode("FileInCollection", init)));
    }

    @Test
    public void testJavaIterator() throws IOException {
        final GraphNode hasNext = new GraphNode("HasNext");
        final GraphNode next = new GraphNode("Next");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        hasNext.addOutput(new GraphMultiTransition("hasNext", hasNext, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        next.addOutput(new GraphMultiTransition("hasNext", next, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        next.addOutput(new GraphSingleTransition("next", next, hasNext, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.JAVA_ITERATOR), List.of(GraphFactory.fromGraphStartNode("JavaIterator", hasNext)));
    }

    @Test
    public void testJavaIteratorWrapper() throws IOException {
        final GraphNode start = new GraphNode("Start");
        final GraphNode hasNext = new GraphNode("HasNext");
        final GraphNode next = new GraphNode("Next");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        start.addOutput(new GraphSingleTransition("init", start, hasNext, ActionType.Input));
        hasNext.addOutput(new GraphMultiTransition("hasNext", hasNext, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        next.addOutput(new GraphMultiTransition("hasNext", next, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        next.addOutput(new GraphSingleTransition("next", next, hasNext, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.JAVA_ITERATOR_WRAPPER), List.of(GraphFactory.fromGraphStartNode("JavaIteratorWrapper", start)));
    }

    @Test
    public void testJavaIteratorWrapperWithGetter() throws IOException {
        final GraphNode start = new GraphNode("Start");
        final GraphNode hasNext = new GraphNode("HasNext");
        final GraphNode next = new GraphNode("Next");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        start.addOutput(new GraphSingleTransition("init", start, hasNext, ActionType.Input));
        hasNext.addOutput(new GraphMultiTransition("hasNext", hasNext, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        hasNext.addOutput(new GraphSingleTransition("getIterator", hasNext, hasNext, ActionType.Input));
        next.addOutput(new GraphMultiTransition("hasNext", next, Set.of(
                new GraphMultiTransition.Decision("true", next),
                new GraphMultiTransition.Decision("false", end)), ActionType.Input));
        next.addOutput(new GraphSingleTransition("next", next, hasNext, ActionType.Input));
        next.addOutput(new GraphSingleTransition("getIterator", next, next, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.JAVA_ITERATOR_WRAPPER_WITH_GETTER), List.of(GraphFactory.fromGraphStartNode("JavaIteratorWrapperWithGetter", start)));
    }

    @Test
    public void testLinearity() throws IOException {
        final GraphNode state0 = new GraphNode("State0");
        final GraphNode state1 = new GraphNode("State1");
        final GraphEndNode end = new GraphEndNode();

        // Create graph
        state0.addOutput(new GraphSingleTransition("a", state0, state1, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("useOther", state0, state0, ActionType.Input));
        state0.addOutput(new GraphSingleTransition("finish", state0, end, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("b", state1, end, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("useOther", state1, state1, ActionType.Input));
        state1.addOutput(new GraphSingleTransition("finish", state1, end, ActionType.Input));

        // Test
        graphEquals(List.of(Tests.Basic.LINEARITY), List.of(GraphFactory.fromGraphStartNode("Linearity", state0)));
    }
}
