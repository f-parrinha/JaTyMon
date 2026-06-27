package jatymon.typestate.graph.nodes;

import jatymon.typestate.graph.transitions.GraphMultiTransition;
import jatymon.typestate.graph.transitions.GraphSingleTransition;
import jatymon.typestate.graph.transitions.GraphTransition;

import java.util.*;

public class GraphNode {
    protected final String name;
    protected final Map<String, GraphTransition> outputs;
    protected final Map<String, GraphTransition> inputs;

    public GraphNode(String name) {
        this.name = name;
        this.outputs = new HashMap<>();
        this.inputs = new HashMap<>();
    }

    public void addOutput(final GraphTransition transition) {
        final String transitionName = transition.getName();
        if (outputs.containsKey(transitionName)) {
            return;
        }

        // Update outputs state and transition destinations' inputs state
        outputs.put(transitionName, transition);
        if (transition instanceof GraphSingleTransition singleTransition) {
            final GraphNode destination = singleTransition.getOut();
            destination.addInput(transition);
        } else if (transition instanceof GraphMultiTransition multiTransition) {
            final Collection<GraphNode> destinations = multiTransition.getOuts().values();
            for (var destination : destinations) {
                destination.addInput(transition);
            }
        }
    }

    public void addInput(GraphTransition transition) {
        final String transitionSignature = transition.id();
        if (inputs.containsKey(transitionSignature)) {
            return;
        }

        // Update inputs state and transition input's outputs state
        inputs.put(transitionSignature, transition);
        final GraphNode input = transition.getIn();
        input.addOutput(transition);
    }

    public String getName() {
        return name;
    }

    public Map<String, GraphTransition> getOutputs() {
        return Map.copyOf(outputs);
    }

    public Map<String, GraphTransition> getInputs() {
        return Map.copyOf(inputs);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GraphNode other &&
                other.getName().equals(getName()) &&
                other.inputs.equals(inputs) &&
                other.outputs.equals(outputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), inputs, outputs);
    }
}
