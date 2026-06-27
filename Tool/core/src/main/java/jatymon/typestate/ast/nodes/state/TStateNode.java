package jatymon.typestate.ast.nodes.state;

import jatymon.common.*;
import jatymon.common.ActionSignature;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.action.TActionNode;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.typestate.DuplicateMethodDiagnostic;
import jatymon.diagnostics.semantic.internalstate.InvalidRatioSumDiagnostic;
import jatymon.diagnostics.semantic.internalstate.InvalidRatioValueDiagnostic;
import jatymon.diagnostics.semantic.typestate.UnexpectedEmptyStateDiagnostic;

import java.util.*;

public class TStateNode extends TNode implements Nameable {
    public static final String TYPE = "state";
    public static final String END_STATE = "end";

    protected final String name;
    protected final List<TActionNode> inputActions;
    protected final List<TActionNode> outputActions;

    public TStateNode(final TokenPosition tokenPos,
                      final String name,
                      final List<TActionNode> inputActions,
                      final List<TActionNode> outputActions) {
        super(tokenPos);
        this.name = name;
        this.inputActions = inputActions;
        this.outputActions = outputActions;
    }

    public static TStateNode fromAnonymousState(final TokenPosition tokenPosition, final String name, final TAnonymousStateNode anonNode) {
        return new TStateNode(tokenPosition, name, anonNode.inputActions, anonNode.outputActions);
    }

    public String getName() {
        return name;
    }

    public List<TActionNode> getActions() {
        final List<TActionNode> actions = new LinkedList<>();
        actions.addAll(inputActions);
        actions.addAll(outputActions);
        return actions;
    }
    public List<TActionNode> getInputActions() {
        return Collections.unmodifiableList(inputActions);
    }
    public List<TActionNode> getOutputActions() {
        return Collections.unmodifiableList(outputActions);
    }

    @Override
    public List<TNode> getChildren() {
        return Collections.unmodifiableList(getActions());
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<TActionNode> actions = getActions();
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final TokenPosition tokenPosition = getTokenPosition();
        final String protocolName = ctx.getTypestateName();

        // Check for empty state
        if (actions.isEmpty()) {
            diagnostics.add(new UnexpectedEmptyStateDiagnostic(protocolName, tokenPosition, name));
        }

        // Check for duplicate methods
        final Set<ActionSignature> seen = new HashSet<>();
        for (final TActionNode action : actions) {
            if (!seen.add(action.signature())) {
                diagnostics.add(new DuplicateMethodDiagnostic(protocolName, tokenPosition));
            }
        }

        // Validate ratios
        double sum = 0f;
        boolean computeRatios = false;
        for(TActionNode action : actions) {
            final Ratio ratio = action.getRatio();
            if (ratio instanceof NullRatio) {
                continue;
            }
            if (!ratio.isValid()) {
                diagnostics.add(new InvalidRatioValueDiagnostic(protocolName, tokenPosition, ratio.getValue()));
            }

            computeRatios = true;
            sum += ratio.getValue();
        }
        if (sum != 1f && computeRatios) {
            diagnostics.add(new InvalidRatioSumDiagnostic(sum, name, protocolName, tokenPosition));
        }
        return diagnostics;
    }

    @Override
    public String toString() {
        return String.format("[TStateNode {name: %s, inputActions: %s, outputActions: %s}]", name, inputActions, outputActions);
    }

    @Override
    public boolean equals(Object obj) {
        return  obj instanceof TStateNode other &&
                super.equals(other) &&
                other.inputActions.equals(inputActions) &&
                other.outputActions.equals(outputActions) &&
                other.name.equals(name);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        json.put(JsonKeys.ACTIONS, JsonSerializable.fromList(getActions()));
        return json;
    }
}
