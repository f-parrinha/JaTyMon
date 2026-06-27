package jatymon.exceptions.graph;

public class UnexpectedDecisionStateException extends RuntimeException {
    public static String MESSAGE = "Unexpected decision state found in a decision state.";
    public UnexpectedDecisionStateException() {
        super(MESSAGE);
    }
}
