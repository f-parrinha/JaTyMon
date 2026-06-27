package jatymon.exceptions.graph;

public class UnknownDestinationTypeException extends RuntimeException {
    public static final String MESSAGE = "Unknown destination type found while building graph. A destination, an AST node, must be either TIdNode, TStateNode, or TDecisionStateNode.";
    public UnknownDestinationTypeException() {
        super(MESSAGE);
    }
}
