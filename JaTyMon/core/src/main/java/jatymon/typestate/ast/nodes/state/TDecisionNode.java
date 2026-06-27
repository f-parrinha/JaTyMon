package jatymon.typestate.ast.nodes.state;

import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.decisions.UnexpectedDecisionLabelDiagnostic;
import jatymon.exceptions.typestate.InvalidDecisionNodeDestination;
import jatymon.typestate.ast.nodes.ref.TIdNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TDecisionNode extends TNode implements Decision {
    public static final String TYPE = "decision";

    private final String label;
    private final TNode destination;            /* TIdNode | TStateNode */
    private Set<String> expectedDecisionLabels;

    public TDecisionNode(final TokenPosition tokenPos, final String label, final TNode destination) throws InvalidDecisionNodeDestination {
        super(tokenPos);

        if (destination instanceof TIdNode || destination instanceof TStateNode) {
            this.label = label;
            this.destination = destination;
        } else {
            throw new InvalidDecisionNodeDestination();
        }
    }

    public void setExpectedDecisionLabels(Set<String> expectedDecisionLabels) {
        this.expectedDecisionLabels = Set.copyOf(expectedDecisionLabels);
    }

    public String getLabel() {
        return label;
    }

    public TNode getDestination() {
        return destination;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        List<AbstractDiagnostic> diagnostics = new LinkedList<>();

        // Invalid label, e.g. does not match the return type in the parent (method).
        if (!expectedDecisionLabels.contains(label) && !expectedDecisionLabels.isEmpty()) {
            diagnostics.add(new UnexpectedDecisionLabelDiagnostic(ctx.getTypestateName(), getTokenPosition(), label));
        }

        return diagnostics;
    }

    @Override
    public String toString() {
        return String.format("[TDecisionNode {label: %s, destination: %s}]", label, destination);
    }

    @Override
    public List<TNode> getChildren() {
        return List.of(destination);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TDecisionNode other &&
                super.equals(other) &&
                other.label.equals(label) &&
                other.destination.equals(destination);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.LABEL, label);
        json.put(JsonKeys.DESTINATION, destination.toJson());
        return json;
    }
}
