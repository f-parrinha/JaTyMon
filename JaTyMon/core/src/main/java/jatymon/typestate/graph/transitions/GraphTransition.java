package jatymon.typestate.graph.transitions;

import jatymon.actions.ActionType;
import jatymon.common.ActionSignature;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.typestate.graph.nodes.GraphNode;

import java.util.*;

/**
 * Abstract class {@code GraphTransition} describes a transition in the graph containing all associated elements
 *  (e.g., method name and parameters, ratio, assignments, predicates, etc...)
 * @author Francisco Parrinha
 */
public abstract class GraphTransition {
    protected final String name;
    protected final GraphNode in;
    protected final ActionType actionType;
    protected Ratio ratio;
    protected Set<String> preAssignments;
    protected Set<String> predicates;
    protected Set<String> postAssignments;
    protected String returnType;
    protected List<String> args;

    public GraphTransition (final String name, final GraphNode in, final ActionType actionType) {
        this.name = name;
        this.in = in;
        this.actionType = actionType;
        this.ratio = new NullRatio();
        this.preAssignments = new TreeSet<>();
        this.predicates = new TreeSet<>();
        this.postAssignments = new TreeSet<>();
        this.args = new LinkedList<>();
    }


    /**
     * A String containing input:method_name:output
     * @return transition unique signature
     */
    public abstract String id();

    public ActionSignature signature() {
        return new ActionSignature(name, args);
    }

    /**
     * Returns the transition's name (or method name)
     * @return transition name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the input graph node (or state). This is from where the transition is triggered.
     * @return input graph node
     */
    public GraphNode getIn() {
        return in;
    }

    /**
     * Returns the transition's action type (input or output)
     * @return action type
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Returns transition's ratio. This ratio may be {@code NullRatio} to represent inexistence of ratio.
     * @return associated ratio (possibly {@code NullRatio});
     */
    public Ratio getRatio() {
        return ratio;
    }

    /**
     * Returns the (possibly empty) set of associated pre-assignments
     * @return pre-assignments set
     */
    public Set<String> getPreAssignments() {
        return preAssignments;
    }

    /**
     * Returns the (possibly empty) set of associated post-assignments
     * @return post-assignments set
     */
    public Set<String> getPostAssignments() {
        return postAssignments;
    }

    /**
     * Returns the (possibly empty) set of associated predicates
     * @return predicates set
     */
    public Set<String> getPredicates() {
        return predicates;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GraphTransition other &&
                other.id().equals(id()) &&
                other.in.getName().equals(in.getName()) &&
                other.actionType.equals(actionType) &&
                other.ratio.equals(ratio) &&
                other.preAssignments.equals(preAssignments) &&
                other.postAssignments.equals(postAssignments) &&
                other.predicates.equals(predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }

}
