package jatymon.typestate.ast.nodes.state;

import jatymon.common.JsonSerializable;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.decisions.DuplicateDecisionLabelDiagnostic;
import jatymon.diagnostics.semantic.decisions.EnumerateAllDecisionsDiagnostic;

import java.util.*;

public class TDecisionStateNode extends TNode implements Decision {
    public static final String TYPE = "decisionState";

    private final List<TDecisionNode> decisions;
    private Set<String> expectedDecisionLabels;
    public TDecisionStateNode(final TokenPosition tokenPos, final List<TDecisionNode> decisions) {
        super(tokenPos);
        this.decisions = List.copyOf(decisions);
        this.expectedDecisionLabels = new HashSet<>();
    }

    public void setExpectedDecisionLabels(final Set<String> expectedDecisionLabels) {
        this.expectedDecisionLabels = Set.copyOf(expectedDecisionLabels);
    }

    public List<TDecisionNode> getDecisions() {
        return decisions;
    }

    public List<String> getLabels() {
        final List<String> labels = new ArrayList<>();
        for (var decision : decisions) {
            labels.add(decision.getLabel());
        }
        return labels;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();

        // Validate types
        final Set<String> seenLabels = new HashSet<>();
        int correctFoundLabelCount = 0;
        for (var decision : decisions) {
            decision.setExpectedDecisionLabels(expectedDecisionLabels);

            final String label = decision.getLabel();
            if (!seenLabels.add(label)) {
                diagnostics.add(new DuplicateDecisionLabelDiagnostic(ctx.getTypestateName(), getTokenPosition(), label));
            } else if (expectedDecisionLabels.contains(label)) {
                correctFoundLabelCount++;
            }
        }

        // Enumeration errors
        if (correctFoundLabelCount < expectedDecisionLabels.size()) {
            diagnostics.add(new EnumerateAllDecisionsDiagnostic(ctx.getTypestateName(), getTokenPosition()));
        }

        return diagnostics;
    }

    @Override
    public String toString() {
        return String.format("[TDecisionStateNode {decisions: %s}]", decisions);
    }

    @Override
    public List<TNode> getChildren() {
        return new ArrayList<>(decisions);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof TDecisionStateNode other &&
                super.equals(other) &&
                other.decisions.equals(decisions);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.DECISIONS, JsonSerializable.fromList(decisions));
        return json;
    }
}
