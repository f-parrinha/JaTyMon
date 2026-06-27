package jatymon.babelprojects.multipaxos.session;

import jatymon.babelprojects.multipaxos.exceptions.UnknownOperationException;
import jatymon.babelprojects.multipaxos.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.util.*;

/**
 * Class {@code Session} keeps track of operations that a replica may do. Particularly useful in the Babel's event-driven environment
 */
public class Session {
    private final Map<String, Operation> ops;

    public Session() {
        ops = new HashMap<>();
    }

    /**
     * Generates a new unique id for a new operation
     * @return unique id
     */
    public static String createOpId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Starts a new WRITE operation
     * @param clientHost client host
     * @param ballot current highest prepare
     * @param value value to write
     * @return operation id
     */
    public String startWrite(final String clientId,
                             final Host clientHost,
                             final int slot,
                             final int ballot,
                             final byte[] value) {
        final String opId = createOpId();
        ops.put(opId, new Operation.Builder()
                .withOpId(opId)
                .withClientId(clientId)
                .withClientHost(clientHost)
                .withSlot(slot)
                .withBallot(ballot)
                .withValue(value)
                .withType(Operation.Type.WRITE)
                .build());
        return opId;
    }

    /**
     * Starts a new READ operation
     * @param clientHost client host
     * @param ballot current highest prepare
     * @return operation id
     */
    public String startRead(final String clientId, final Host clientHost, final int slot, final int ballot) {
        final String opId = createOpId();
        ops.put(opId, new Operation.Builder()
                .withOpId(opId)
                .withClientId(clientId)
                .withClientHost(clientHost)
                .withSlot(slot)
                .withBallot(ballot)
                .withType(Operation.Type.READ)
                .build());
        return opId;
    }

    /**
     * Creates a new operation equals to the given one but with a new id.
     * @param op reference operation
     * @return operation id
     */
    public String restart(final Operation op) {
        final String opId = createOpId();
        ops.put(opId, new Operation.Builder()
                .withOpId(opId)
                .withClientId(op.clientId)
                .withClientHost(op.clientHost)
                .withSlot(op.slot)
                .withBallot(op.ballot)
                .withValue(op.value)
                .withType(op.type)
                .withRetryTimerId(op.retryTimerId)
                .build());
        return opId;
    }

    /**
     * Stops and clears the operation by the given id
     * @param opId operation id
     * @return stopped operation instance
     */
    public Operation stop(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.remove(opId);
    }

    /**
     * Clears the set of received messages for an operation with the given id
     * @param opId operation id
     * @return set with removed messages
     */
    public Set<ReplicaMessage> clearReceived(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        final Operation op = ops.get(opId);
        final Set<ReplicaMessage> res = Set.copyOf(op.received);
        op.received.clear();
        return res;
    }

    /**
     * Adds a new message to the received set of an operation with the given id
     * @param opId operation id
     * @param message received message
     * @return true if the message was not previously received (duplicated)
     */
    public boolean receiveMessage(final String opId, final ReplicaMessage message) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId).receive(message);
    }

    /**
     * Checks if the size of the set of received messages for an operation with the given id meets the given quorum threshold
     * @param opId operation id
     * @param threshold quorum threshold
     * @return true if has quorum
     */
    public boolean hasQuorum(final String opId, final int threshold) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId).hasQuorum(threshold);
    }

    /**
     * Returns whether the session contains an operation with the given id or not
     * @param opId operation id
     * @return true if it contains, false if not
     */
    public boolean hasOp(final String opId) {
        return ops.containsKey(opId);
    }

    /**
     * Returns the operation instance with the given id
     * @param opId operation id
     * @return operation instance
     */
    public Operation getOp(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId);
    }
}
