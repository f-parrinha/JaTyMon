package jatymon.actions;

import java.util.HashMap;
import java.util.Map;

/**
 * Class {@code SamplesManager} is responsible for keeping track of the number of types actions in a typestate were executed.
 *  This is, it keeps track of the total number of action executions in a state, and the total number of times an action
 *  in a state was executed.
 * @author Francisco Parrinha
 */
public class ActionsCounter {
    private final Map<String, Integer> stateActionsCount;
    private final Map<ActionId, Integer> actionCounts;

    public ActionsCounter() {
        stateActionsCount = new HashMap<>();
        actionCounts = new HashMap<>();
    }

    /**
     * Increments the corresponding count values according to the given sample id
     * @param actionId reference sample id. Tuple representing a method in a state
     */
    public void sample(final ActionId actionId) {
        stateActionsCount.merge(actionId.stateName(), 1, Integer::sum);
        actionCounts.merge(actionId, 1, Integer::sum);
    }

    /**
     * Returns the total number of action executions that occurred in the given state
     * @param stateName reference state
     * @return number of action executions in the given state
     */
    public int getStateActionsCount(final String stateName) {
        return stateActionsCount.getOrDefault(stateName, 0);
    }

    /**
     * Returns the total number of times an action in a state has been executed.
     * @param actionId reference action id. Tuple {@code (state_name, method_name)}
     * @return total number of times an action in a state was executed
     */
    public int getActionCount(final ActionId actionId) {
        return actionCounts.getOrDefault(actionId, 0);
    }
}
