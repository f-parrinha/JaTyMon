package jatymon.typestate.ast.nodes.declaration;

import jatymon.common.JsonSerializable;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.TPredNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TExtNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TValNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.key.TKeyNode;
import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.diagnostics.semantic.typestate.DuplicateStateDiagnostic;
import jatymon.diagnostics.semantic.typestate.UnexpectedEmptyProtocolDiagnostic;

import java.util.*;

public class TDeclarationNode extends TNode {
    public static final String TYPE = "declaration";

    private final String name;
    private final TKeyNode key;
    private final List<TExtNode> exts;
    private final List<TValNode> vals;
    private final List<TAssignNode> assigns;
    private final List<TPredNode> preds;
    private final List<TStateNode> states;

    private TDeclarationNode(final TokenPosition tokenPos,
                             final String name,
                             final TKeyNode key,
                             final List<TExtNode> exts,
                             final List<TValNode> vals,
                             final List<TAssignNode> assigns,
                             final List<TPredNode> preds,
                             final List<TStateNode> states) {
        super(tokenPos);
        this.name = name;
        this.key = key;
        this.exts = List.copyOf(exts);
        this.vals = List.copyOf(vals);
        this.assigns = List.copyOf(assigns);
        this.preds = List.copyOf(preds);
        this.states = List.copyOf(states);
    }

    public String getName() {
        return name;
    }

    public TKeyNode getKey() {
        return key;
    }

    public List<TAssignNode> getAssigns() {
        return assigns;
    }

    public List<TPredNode> getPreds() {
        return preds;
    }

    public List<TExtNode> getExts() {
        return exts;
    }

    public List<TValNode> getVals() {
        return vals;
    }

    public List<TStateNode> getStates() {
        return states;
    }

    public boolean hasInternalState() {
        return !(exts.isEmpty() && vals.isEmpty() && assigns.isEmpty() && preds.isEmpty());
    }

    @Override
    public List<TNode> getChildren() {
        List<TNode> children = new ArrayList<>();
        children.addAll(exts);
        children.addAll(vals);
        if (key != null) children.add(key);
        children.addAll(assigns);
        children.addAll(preds);
        children.addAll(states);
        return children;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final TokenPosition tokenPosition = getTokenPosition();
        final String protocolName = ctx.getTypestateName();
        if (states.isEmpty()) {
            diagnostics.add(new UnexpectedEmptyProtocolDiagnostic(protocolName, tokenPosition));
        }

        // Check for duplicate states
        for (final TStateNode state : states) {
            if (!ctx.addState(state)) {
                diagnostics.add(new DuplicateStateDiagnostic(protocolName, tokenPosition, state));
            }
        }

        return diagnostics;
    }

    @Override
    public String toString() {
        return String.format("[TDeclarationNode {name: %s, exts: %s, vals: %s, assigns: %s, preds: %s states: %s}]",
                name, exts, vals, assigns, preds, states);
    }

    @Override
    public boolean equals(final Object obj) {
        return  obj instanceof TDeclarationNode other &&
                super.equals(other) &&
                other.assigns.equals(assigns) &&
                other.preds.equals(preds) &&
                other.vals.equals(vals) &&
                other.exts.equals(exts) &&
                other.states.equals(states) &&
                other.name.equals(name);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.NAME, name);
        if (key != null) json.put(JsonKeys.KEY, key.getName());
        if (hasInternalState()) {
            List<TNode> fields = new ArrayList<>();
            fields.addAll(exts);
            fields.addAll(vals);
            fields.addAll(assigns);
            fields.addAll(preds);
            json.put(JsonKeys.INTERNAL_STATE, JsonSerializable.fromList(fields));
        }
        json.put(JsonKeys.STATES, JsonSerializable.fromList(states));
        return json;
    }

    public static class Builder {
        private TokenPosition tokenPos;
        private String name;
        private TKeyNode key;
        private List<TExtNode> exts;
        private List<TValNode> vals;
        private List<TAssignNode> assigns;
        private List<TPredNode> preds;
        private List<TStateNode> states;

        public Builder withTokenPos(final TokenPosition tokenPos) {
            this.tokenPos = tokenPos;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withKey(final TKeyNode key) {
            this.key = key;
            return this;
        }

        public Builder withExts(final List<TExtNode> exts) {
            this.exts = exts;
            return this;
        }

        public Builder withVals(final List<TValNode> vals) {
            this.vals = vals;
            return this;
        }

        public Builder withAssigns(final List<TAssignNode> assigns) {
            this.assigns = assigns;
            return this;
        }

        public Builder withPreds(final List<TPredNode> preds) {
            this.preds = preds;
            return this;
        }

        public Builder withStates(final List<TStateNode> states) {
            this.states = states;
            return this;
        }

        public TDeclarationNode build() {
            return new TDeclarationNode(tokenPos, name, key, exts, vals, assigns, preds, states);
        }
    }
}
