package jatymon.exceptions.graph;

import jatymon.typestate.ast.nodes.TNode;

public class ExpectedDecisionStateDestinationException extends RuntimeException {
    public static final String MESSAGE = "Destination <%s> must be of type TDecisionStateNode";
    public ExpectedDecisionStateDestinationException(final TNode destination) {
        super(MESSAGE.formatted(destination));
    }
}
