package jatymon.typestate.graph.transitions;

import jatymon.ratios.Ratio;

import java.util.List;
import java.util.Set;

/**
 * Interface {@code GraphTransitionBuilder} is the blueprint of all builders for all types of transitions.
 *  Transitions have customizable fields: ratios, pre-assignments, post-assignments and predicates. The builders
 *  allow the customization of these non-required features.
 */
public interface GraphTransitionBuilder {
    GraphTransitionBuilder withRatio(final Ratio ratio);
    GraphTransitionBuilder withPreAssignments(final Set<String> preAssignments);
    GraphTransitionBuilder withPostAssignments(final Set<String> postAssignments);
    GraphTransitionBuilder withPredicates(final Set<String> predicates);
    GraphTransitionBuilder withReturnType(final String returnType);
    GraphTransitionBuilder withArgs(final List<String> args);
}
