package jatymon.typestate.ast;

import jatymon.processing.TypestateClass;
import jatymon.resolving.Resolver;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.TNode;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TValNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.TPredNode;
import jatymon.typestate.ast.nodes.key.TKeyNode;
import jatymon.typestate.ast.nodes.state.TStateNode;

import java.util.*;

public class TypestateAstValidator {
    public static List<AbstractDiagnostic> validate(final TypestateAst ast,
                                                    final Resolver resolver,
                                                    final Context ctx) {
        final List<AbstractDiagnostic> diagnostics = new LinkedList<>();
        final Queue<TNode> pending = new LinkedList<>(ast.root().getChildren());
        while(!pending.isEmpty()) {
            final TNode node = pending.poll();
            pending.addAll(node.getChildren());
            diagnostics.addAll(node.validateSelf(resolver, ctx));
        }
        return diagnostics;
    }

    /**
     * Class {@code TypestateAstValidator.Context} stores information regarding the typestate such as name, imports, fields, assignments,
     *  states, etc... The information is filled during AST semantic validation process, used for the semantic validation process, and by
     *  later processing stages.
     * @author Francisco Parrinha
     */
    public static class Context {
        private final TypestateClass typestateClass;
        private final String typestateName;
        private final String className;
        private final String classQfName;
        private final Map<String, TStateNode> states;
        private final Set<String> imports;
        private final Set<String> exts;
        private final Map<String, TValNode> vals;
        private final Map<String, TAssignNode> assignments;
        private final Map<String, TPredNode> predicates;

        private TKeyNode key;
        private TStateNode firstState;
        private String pkgQfName;

        private Context(final String typestateName,
                        final String defaultPkgQfName,
                        final String classQfName,
                        final String className,
                        final TypestateClass typestateClass) {
            this.typestateName = typestateName;
            this.pkgQfName = defaultPkgQfName;
            this.className = className;
            this.classQfName = classQfName;
            this.typestateClass = typestateClass;
            this.states = new HashMap<>();
            this.imports = new HashSet<>();
            this.exts = new HashSet<>();
            this.vals = new HashMap<>();
            this.assignments = new HashMap<>();
            this.predicates = new HashMap<>();
        }

        /* -------------- ADD -------------- */

        /**
         * Adds a new import, if it does not already exist.
         * @param importQfName import qualified name
         * @return true if added, false if not
         */
        public boolean addImport(final String importQfName) {
            return imports.add(importQfName);
        }

        /**
         * Adds the given state, if it does not already exist. Moreover, the variable {@code firstState} is also set to the
         *  given state if it's internal value is null.
         * @param state state to add
         * @return true if added, false if not
         */
        public boolean addState(final TStateNode state) {
            final String stateName = state.getName();
            if (states.containsKey(stateName)) {
                return false;
            }

            firstState = firstState == null ? state : firstState;
            states.put(stateName, state);
            return true;
        }

        /**
         * Adds a new assignment if it does not already exist.
         * @param assignNode AST {@code Assign} node
         * @return true if added, false if not
         */
        public boolean addAssignment(final TAssignNode assignNode) {
            final String name = assignNode.getName();
            if (assignments.containsKey(name)) {
                return false;
            }
            assignments.put(name, assignNode);
            return true;
        }


        /**
         * Adds a new predicate if it does not already exist.
         * @param predNode AST {@code Pred} node
         * @return true if added, false if not
         */
        public boolean addPredicate(final TPredNode predNode) {
            final String name = predNode.getName();
            if (predicates.containsKey(name)) {
                return false;
            }
            predicates.put(name, predNode);
            return true;
        }

        /**
         * Adds a new {@code val} field if it does not already exist.
         * @param valNode AST {@code val} node
         * @return true if added, false if not
         */
        public boolean addVal(final TValNode valNode) {
            final String name = valNode.getName();
            if (vals.containsKey(name)) {
                return false;
            }
            vals.put(name, valNode);
            return true;
        }

        /**
         * Adds a new {@code ext} field name if it does not already exist.
         * @param extName import qualified name
         * @return true if added, false if not
         */
        public boolean addExt(final String extName) {
            return exts.add(extName);
        }


        /* -------------- CONTAINS -------------- */


        /**
         * Checks whether a given {@code val} field has already been processed
         * @param valName Val field name
         * @return true if it contains, false if not
         */
        public boolean containsVal(final String valName) {
            return vals.containsKey(valName);
        }

        /**
         * Checks whether a given {@code ext} field has already been processed
         * @param extName Val field name
         * @return true if it contains, false if not
         */
        public boolean containsExt(final String extName) {
            return exts.contains(extName);
        }

        /**
         * Returns if the typestate contains the given field name, this is, a declared {@code val} or {@code ext} with that name.
         * @param fieldName field name
         * @return true if it contains the field, false if not
         */
        public boolean containsField(final String fieldName) {
            return vals.containsKey(fieldName) || exts.contains(fieldName);
        }

        /**
         * Checks whether a given assignment name has already been processed
         * @param assignmentName assignment name
         * @return true if it contains, false if not
         */
        public boolean containsAssignment(final String assignmentName) {
            return assignments.containsKey(assignmentName);
        }

        /**
         * Checks whether a given predicate name has already been processed
         * @param predicateName predicate name
         * @return true if it contains, false if not
         */
        public boolean containsPredicate(final String predicateName) {
            return predicates.containsKey(predicateName);
        }

        /**
         * Checks whether the given state node is defined in the protocol. Supports anonymous states.
         * @param stateNode state node to validate.
         * @return true if it contains the given state, false if not
         */
        public boolean containsState(final TStateNode stateNode) {
            return states.containsKey(stateNode.getName());
        }

        /**
         * Tries to find if there is a state with the given name.
         * @param stateName state name to find
         * @return true if found, false if not
         */
        public boolean containsState(final String stateName) {
            return states.containsKey(stateName);
        }


        /* -------------- SETTERS -------------- */

        /**
         * Sets a new package name
         * @param pkgName new package name
         */
        public void setPackage(final String pkgName) {
            this.pkgQfName = pkgName;
        }

        /**
         * Sets the key name of a typestate (which can be null). We can track different current states for different entites using a key type.
         *  Suppose there are multiple clients, the typestate tracks the current state of different client id's (the key type)
         * @param key key AST node
         */
        public void setKey(TKeyNode key) {
            this.key = key;
        }

        /* -------------- GETTERS -------------- */


        /**
         * Returns the typestate's name.
         * @return typestate name
         */
        public String getTypestateName() {
            return typestateName;
        }

        /**
         * Returns the qualified name of the typestate
         * @return {pkgQfName}.{typestateName}
         */
        public String getTypestateQfName() {
            return String.format("%s.%s", pkgQfName, typestateName);
        }

        /**
         * Returns the annotated class's qualified name.
         * @return annotated class qualified name as String
         */
        public String getClassQfName() {
            return classQfName;
        }


        /**
         * Returns the annotated class's name.
         * @return annotated class qualified name as String
         */
        public String getClassName() {
            return className;
        }

        /**
         * Returns a copy of the defined states as a list.
         * @return defined states as list
         */
        public Set<TStateNode> getStates() {
            return Set.copyOf(states.values());
        }

        /**
         * Returns the first state that was found during validation.
         * @return the first AST TStateNode node.
         */
        public TStateNode getFirstState() {
            return firstState;
        }

        /**
         * Returns the set of loaded (declared) imports during validation processing
         * @return the loaded set of imports
         */
        public Set<String> getImports() {
            return Set.copyOf(imports);
        }

        /**
         * Returns the typestate's package qualified name. Does not include {@code * } at the end if the package uses a wildcard
         * @return package qualified name (E.g., my.pkg.test)
         */
        public String getPackageQfName() {
            return pkgQfName;
        }


        /**
         * Returns an immutable set containing all found assignments
         * @return immutable set
         */
        public Map<String, TAssignNode> getAssignments() {
            return Map.copyOf(assignments);
        }

        /**
         * Returns an immutable set containing all found assignments
         * @return immutable set
         */
        public Map<String, TPredNode> getPredicates() {
            return Map.copyOf(predicates);
        }

        /**
         * Returns an immutable map storing the {@code vals} belonging to the typestate
         * @return immutable {@code vals} map
         */
        public Map<String, TValNode> getVals() {
            return Map.copyOf(vals);
        }

        /**
         * Returns an immutable set storing the {@code exts} belonging to the typestate
         * @return immutable {@code exts} map
         */
        public Set<String> getExts() {
            return Set.copyOf(exts);
        }

        /**
         * Returns the associated typestate key type (which can possibly be null, meaning will typestates not map current states per key)
         * @return associated key AST node
         */
        public TKeyNode getKey() {
            return key;
        }

        /**
         * Returns the @Typestate annotated class
         * @return typestate annotated class
         */
        public TypestateClass getTypestateClass() {
            return typestateClass;
        }


        public static final class Builder {
            private String typestateName;
            private String defaultPkgQfName;
            private String classQfName;
            private String className;
            private TypestateClass typestateClass;

            public Builder withTypestateName(final String typestateName) {
                this.typestateName = typestateName;
                return this;
            }
            public Builder withDefaultPkgQfName(final String defaultPkgQfName) {
                this.defaultPkgQfName = defaultPkgQfName;
                return this;
            }
            public Builder withClassQfName(final String classQfName) {
                this.classQfName = classQfName;
                return this;
            }
            public Builder withClassName(final String className) {
                this.className = className;
                return this;
            }
            public Builder withTypestateClass(final TypestateClass typestateClass) {
                this.typestateClass = typestateClass;
                return this;
            }
            public Context build() {
                return new Context(typestateName, defaultPkgQfName, classQfName, className, typestateClass);
            }
        }
    }
}
