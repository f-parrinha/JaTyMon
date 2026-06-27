package jatymon.actions;

/**
 * Record {@code SampleId} identifies typestate action executions triggered by the method {@code actionName}
 *  in the state {@code stateName}. It is used to keep track of the number of times a method in a particular state was called.
 * @param stateName
 * @param actionName
 */
public record ActionId(String stateName, String actionName, ActionType actionType) {

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof ActionId(final String otherStateName, final String otherMethodName, final ActionType otherType) &&
                stateName.equals(otherStateName) &&
                actionName.equals(otherMethodName);
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(stateName, actionName);
    }
}
