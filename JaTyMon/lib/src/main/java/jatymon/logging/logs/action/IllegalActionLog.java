package jatymon.logging.logs.action;

import jatymon.actions.ActionType;
import jatymon.logging.logs.ErrorLog;

public class IllegalActionLog extends ErrorLog {
    public static final String MESSAGE = "Illegal %s action \"%s\" executed in state \"%s\"";

    private final String currentState;
    private final String method;

    public IllegalActionLog(final String typestateName, final String currentState, final String method, final ActionType actionType) {
        super(typestateName, MESSAGE.formatted(actionType.toString().toLowerCase(), method, currentState));
        this.currentState = currentState;
        this.method = method;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof IllegalActionLog log && super.equals(log) &&
                log.currentState.equals(currentState) &&
                log.method.equals(method);
    }
}
