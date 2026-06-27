package jatymon.exceptions.typestate;

public class InvalidDecisionNodeDestination extends RuntimeException {
    public static final String MESSAGE = "Destination is neither a State node nor an Id node";
    public InvalidDecisionNodeDestination() {
        super(MESSAGE);
    }
}
