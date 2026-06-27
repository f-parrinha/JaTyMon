package graph;

import common.compilation.CompilationResult;
import common.compilation.CompilationTester;
import jatymon.processing.ProcessorOptions;
import jatymon.typestate.TypestateData;
import jatymon.typestate.graph.Graph;
import jatymon.typestate.graph.nodes.GraphNode;
import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTester extends CompilationTester {

    /**
     * Prints a debug text for the graph
     * @return debug text as output
     * @param graph the {@code Graph} instance to debug
     */
    public static String printGraph(final Graph graph) {
        final Map<String, GraphNode> asMap = graph.asMap();
        final StringBuilder sb = new StringBuilder();
        sb.append("Protocol ").append(graph.getProtoName()).append("\n");
        sb.append("Start Node: ").append(graph.getStart()).append("\n");
        for (GraphNode node : asMap.values()) {
            sb.append("State ").append(node.getName()).append(":\n");
            sb.append("  Inputs:\n");
            for (GraphTransition transition : node.getInputs().values()) {
                appendTransition(sb, transition, "    ");
            }
            sb.append("  Outputs:\n");
            for (GraphTransition transition : node.getOutputs().values()) {
                appendTransition(sb, transition, "    ");
            }
        }
        return sb.toString();
    }


    public void failure(final boolean isSilent,
                        final List<String> filesPath) throws IOException {
        final CompilationResult result = compile(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath.toArray(new String[0]));
        assertTrue(result.isSuccess());
        assertFalse(result.processor().isSuccess());
    }

    public void failure(final List<String> filesPath) throws IOException {
        failure(true, filesPath);
    }

    public void noProcessedGraphs(final boolean isSilent, final String... filesPath) throws IOException {
        final CompilationResult result = compile(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath);
        assertTrue(result.isSuccess());

        final Map<String, TypestateData> processingResults = result.processor().getProcessedTypestates();
        assertTrue(processingResults.isEmpty());
    }

    public void noProcessedGraphs(final String... filesPath) throws IOException {
        noProcessedGraphs(true, filesPath);
    }

    public void graphEquals(final boolean isSilent,
                            final List<String> filesPath,
                            final List<Graph> expectedGraphs) throws IOException {
        final CompilationResult result = compile(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath.toArray(new String[0]));
        assertTrue(result.isSuccess());

        // Assert there are as many computed graphs as the expected ones
        final Map<String, TypestateData> processedAnnotated = result.processor().getProcessedTypestates();
        assertEquals(expectedGraphs.size(), processedAnnotated.size());

        for (final Graph expectedGraph : expectedGraphs) {
            final String graphProtoName = expectedGraph.getProtoName();

            /*
             * Completely brute force, but we require the annotated class qualified name to get the typestateData
             *  and we can't have that here
             */
            var matchingEntry = processedAnnotated.entrySet().stream()
                    .filter(entry -> entry.getValue().getGraph().getProtoName().equals(graphProtoName))
                    .findFirst()
                    .orElse(null);
            assertNotNull(matchingEntry, "No processed graph found for protocol name: %s".formatted(graphProtoName));

            // Assert they are the same
            final String annotatedQfName = matchingEntry.getKey();
            final Graph computedGraph = processedAnnotated.get(annotatedQfName).getGraph();
            assertEquals(
                    expectedGraph,
                    computedGraph,
                    "Graph mismatch for protocol '" + graphProtoName + "'\n" +
                            "Expected:\n" + GraphTester.printGraph(expectedGraph) + "\n" +
                            "Actual:\n"   + GraphTester.printGraph(computedGraph)
            );
        }
    }

    public void graphEquals(final List<String> filesPath, final List<Graph> expectedGraphs) throws IOException {
        graphEquals(true, filesPath, expectedGraphs);
    }

    /* ---------------------- AUX METHODS ---------------------- */


    private static void appendTransitionDetails(final StringBuilder sb, final GraphTransition transition) {
        sb.append("actionType=").append(transition.getActionType());
        sb.append(", ratio=").append(transition.getRatio());

        final Set<String> preAssignments = transition.getPreAssignments();
        final Set<String> predicates = transition.getPredicates();
        final Set<String> postAssignments = transition.getPostAssignments();

        if (!preAssignments.isEmpty()) {
            sb.append(", pre=").append(preAssignments);
        }
        if (!predicates.isEmpty()) {
            sb.append(", pred=").append(predicates);
        }
        if (!postAssignments.isEmpty()) {
            sb.append(", post=").append(postAssignments);
        }
    }

    private static void appendTransition(final StringBuilder sb, final GraphTransition transition, final String indent) {
        if (transition instanceof GraphSingleTransition st) {
            sb.append(indent)
                    .append(st.getName())
                    .append("[");
            appendTransitionDetails(sb, st);
            sb.append("]")
                    .append(" -> ")
                    .append(st.getOut().getName())
                    .append("\n");
        } else if (transition instanceof GraphMultiTransition mt) {
            sb.append(indent)
                    .append(mt.getName())
                    .append("[");
            appendTransitionDetails(sb, mt);
            sb.append("] {\n");
            for (var d : mt.getOuts().entrySet()) {
                sb.append(indent).append("  ")
                        .append(d.getKey())
                        .append(" -> ")
                        .append(d.getValue().getName())
                        .append("\n");
            }
            sb.append(indent).append("}\n");
        }
    }
}
