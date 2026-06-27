package jatymon.typestate.ast.nodes.action;

import jatymon.actions.ActionType;
import jatymon.common.ActionSignature;
import jatymon.common.JsonSerializable;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.common.globals.JsonKeys;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.state.TDecisionStateNode;
import jatymon.typestate.ast.TypestateAstValidator;
import jatymon.typestate.ast.nodes.state.TStateNode;
import jatymon.diagnostics.semantic.decisions.ExpectedDecisionStateDiagnostic;
import jatymon.diagnostics.semantic.decisions.UnexpectedDecisionStateDiagnostic;
import jatymon.diagnostics.semantic.internalstate.UndefinedAssignmentDiagnostic;
import jatymon.diagnostics.semantic.internalstate.UndefinedPredicateDiagnostic;
import jatymon.diagnostics.semantic.resolving.UnknownTypeDiagnostic;
import jatymon.diagnostics.semantic.typestate.ExpectedMethodDiagnostic;
import jatymon.diagnostics.semantic.typestate.UndefinedStateDiagnostic;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.typestate.ast.nodes.ref.TIdNode;
import jatymon.common.TokenPosition;
import jatymon.typestate.ast.nodes.ref.TRefNode;
import jatymon.exceptions.UnknownEnumException;

import java.util.*;

public class TActionNode extends TNode {
    public static final String TYPE = "action";

    private final TRefNode returnType;
    private final String name;
    private final List<TRefNode> args;
    private final TNode destination;
    private final ActionType actionType;
    private Ratio ratio;
    private List<String> preAssignmentNames;
    private List<String> predicateNames;
    private List<String> postAssignmentNames;

    public TActionNode(final TokenPosition tokenPos,
                       final TRefNode returnType,
                       final String name,
                       final List<TRefNode> args,
                       final TNode destination,
                       final ActionType actionType) {
        super(tokenPos);
        this.returnType = returnType;
        this.name = name;
        this.args = List.copyOf(args);
        this.destination = destination;
        this.actionType = actionType;
        this.ratio = new NullRatio();
        this.preAssignmentNames = new LinkedList<>();
        this.predicateNames = new LinkedList<>();
        this.postAssignmentNames = new LinkedList<>();
    }

    /**
     * Returns a string with the name and arguments of the function. The return type is not written. It is useful to check for duplicate methods.
     * @return {@code "<method_name>(<args: arg1, arg2, arg3, ...>)"}
     */
    public ActionSignature signature() {
        return new ActionSignature(name, args.stream().map(Object::toString).toList());
    }

    /**
     * Returns the method's return type (a Java type)
     * @return TRefNode for the method's return type
     */
    public TRefNode getReturnType() {
        return returnType;
    }

    /**
     * Returns the method's name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the method's arguments. An immutable list, which can be empty
     * @return an immutable list representing the method's arguments
     */
    public List<TRefNode> getArgs() {
        return args;
    }

    /**
     * Returns the method's args as a string value.
     * @return a list with the method's args full-names
     */
    public List<String> getArgsFullName() {
        return args.stream().map(TRefNode::getFullName).toList();
    }

    /**
     * Returns the destination after the method's call. A destination can be {@code id | state | decision_state}
     * @return destination's TNode
     */
    public TNode getDestination() {
        return destination;
    }

    /**
     * Returns the probability ratio for a method call.
     * @return method's probability ratio
     */
    public Ratio getRatio() {
        return ratio;
    }

    /**
     * Returns the list of all associated pre-assignment names. May contain duplicates.
     * @return list of pre-assignment names
     */
    public List<String> getPreAssignmentNames() {
        return List.copyOf(preAssignmentNames);
    }

    /**
     * Returns the list of all associated post-assignment names. May contain duplicates.
     * @return list of post-assignment names
     */
    public List<String> getPostAssignmentNames() {
        return List.copyOf(postAssignmentNames);
    }

    /**
     * Returns the set of all associated predicate names. May contain duplicates.
     * @return list of predicate names
     */
    public List<String> getPredicateNames() {
        return List.copyOf(predicateNames);
    }

    @Override
    public List<TNode> getChildren() {
        List<TNode> res = new ArrayList<>();
        res.add(returnType);
        res.addAll(args);
        res.add(destination);
        return res;
    }

    @Override
    public List<AbstractDiagnostic> validateSelf(final Resolver resolver,
                                                 final TypestateAstValidator.Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final String typestateName = ctx.getTypestateName();
        final String typestateQfName = ctx.getTypestateQfName();
        final String packageQfName = ctx.getPackageQfName();
        final String classQfName = ctx.getClassQfName();
        final Set<String> imports = ctx.getImports();
        final TokenPosition tokenPos = getTokenPosition();

        // Resolve return type
        boolean allTypesResolved = true;
        final String returnTypeName = returnType.getFullName();
        final String returnTypeQfName = resolver.qualifyName(returnTypeName, imports, packageQfName, typestateQfName);
        if (!resolver.resolveType(returnTypeQfName)) {
            diagnostics.add(new UnknownTypeDiagnostic(typestateName, tokenPos, returnTypeName));
            allTypesResolved = false;
        }

        // Resolve types in the arguments
        final List<String> argsQfNames = new ArrayList<>();
        for (final TRefNode arg : args) {
            final String argName = arg.getFullName();
            final String argQfName = resolver.qualifyName(argName, imports, packageQfName, typestateQfName);
            if (resolver.resolveType(argQfName)) {
                argsQfNames.add(argQfName);
            } else {
                diagnostics.add(new UnknownTypeDiagnostic(typestateName, arg.getTokenPosition(), argName));
                allTypesResolved = false;
            }
        }

        // Check whether the written method exists in the annotated class AFTER all types are resolved!
        if (allTypesResolved && !resolver.resolveMethod(classQfName, name, returnTypeQfName, argsQfNames)) {
            diagnostics.add(new ExpectedMethodDiagnostic(typestateName, tokenPos, name, classQfName));
        }

        // Validate destination
        if (destination instanceof final TDecisionStateNode decisionState) {
            try {
                decisionState.setExpectedDecisionLabels(resolver.getEnumValues(returnTypeQfName));
            } catch (UnknownEnumException e) {
                diagnostics.add(new UnexpectedDecisionStateDiagnostic(typestateName, tokenPos));
            }
        } else if (resolver.resolveEnum(returnTypeQfName)) {
            diagnostics.add(new ExpectedDecisionStateDiagnostic(typestateName, tokenPos));
        } else if (destination instanceof final TIdNode idNode) {
            final String idNodeName = idNode.getName();
            if (!idNodeName.equalsIgnoreCase(TStateNode.END_STATE) && !ctx.containsState(idNodeName)) {
                diagnostics.add(new UndefinedStateDiagnostic(typestateName, tokenPos, idNode.getName()));
            }
        }

        // Validate assignments and predicates
        for (final String preAssignmentName : preAssignmentNames) {
            if (!ctx.containsAssignment(preAssignmentName)) {
                diagnostics.add(new UndefinedAssignmentDiagnostic(preAssignmentName, typestateName, tokenPos));
            }
        }
        for (final String predicateName : predicateNames) {
            if (!ctx.containsPredicate(predicateName)) {
                diagnostics.add(new UndefinedPredicateDiagnostic(predicateName, typestateName, tokenPos));
            }
        }
        for (final String postAssignmentName : postAssignmentNames) {
            if (!ctx.containsAssignment(postAssignmentName)) {
                diagnostics.add(new UndefinedAssignmentDiagnostic(postAssignmentName, typestateName, tokenPos));
            }
        }
        return diagnostics;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return String.format("[TActionNode {returnType: %s, name: %s, args: %s, ratio: %s, actionType: %s, preAssigns: %s, preds: %s, postAssigns: %s, destination: %s}]",
                returnType, name, args.toString(), ratio, actionType, preAssignmentNames, predicateNames, postAssignmentNames, destination);
    }

    @Override
    public boolean equals(final Object obj) {
        return  obj instanceof TActionNode other &&
                super.equals(other) &&
                other.signature().equals(signature()) &&
                other.ratio.equals(ratio) &&
                other.preAssignmentNames.equals(preAssignmentNames) &&
                other.predicateNames.equals(predicateNames) &&
                other.postAssignmentNames.equals(postAssignmentNames) &&
                other.actionType.equals(actionType);
    }

    @Override
    public Map<String, Object> toJson() {
        var json = super.toJson();
        json.put(JsonKeys.TYPE, TYPE);
        json.put(JsonKeys.ACTION_TYPE, actionType);
        json.put(JsonKeys.RETURN_TYPE, returnType.toJson());
        json.put(JsonKeys.NAME, name);
        json.put(JsonKeys.ARGS, JsonSerializable.fromList(args));
        if (!(ratio instanceof NullRatio)) {
            json.put(JsonKeys.RATIO,  ratio.getValue());
        }
        if (!preAssignmentNames.isEmpty()) {
            json.put(JsonKeys.PRE_ASSIGNMENT_NAMES, preAssignmentNames);
        }
        if (!predicateNames.isEmpty()) {
            json.put(JsonKeys.PREDICATE_NAMES, predicateNames);
        }
        if (!postAssignmentNames.isEmpty()) {
            json.put(JsonKeys.POST_ASSIGNMENT_NAMES, postAssignmentNames);
        }
        json.put(JsonKeys.DESTINATION, destination.toJson());
        return json;
    }




    public static class Builder {
        private final TActionNode instance;

        public Builder(final TokenPosition tokenPos,
                       final TRefNode returnType,
                       final String name,
                       final List<TRefNode> args,
                       final TNode destination,
                       final ActionType actionType) {
            this.instance = new TActionNode(tokenPos, returnType, name, args, destination, actionType);
        }

        public Builder withRatio(final Ratio ratio) {
            instance.ratio = ratio;
            return this;
        }

        public Builder withPreAssignmentNames(final List<String> preAssignmentNames) {
            instance.preAssignmentNames = preAssignmentNames;
            return this;
        }

        public Builder withPredicateNames(final List<String> predicateNames) {
            instance.predicateNames = predicateNames;
            return this;
        }

        public Builder withPostAssignmentNames(final List<String> postAssignmentNames) {
            instance.postAssignmentNames = postAssignmentNames;
            return this;
        }

        public TActionNode build() {
            return instance;
        }
    }
}