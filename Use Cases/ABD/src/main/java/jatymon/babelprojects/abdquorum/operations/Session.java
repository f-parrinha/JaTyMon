package jatymon.babelprojects.abdquorum.operations;

import jatymon.babelprojects.abdquorum.exceptions.IllegalOperationException;
import jatymon.babelprojects.abdquorum.exceptions.UnknownOperationException;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Session {
    private final Map<String, Operation> ops;

    public Session() {
        ops = new HashMap<>();
    }

    public static String createOpId() {
        return UUID.randomUUID().toString();
    }

    public String start(final Operation op) {
        final String clientId = op.getClientId();
        final Host clientHost = op.getClientHost();
        return switch (op) {
            case WriteOperation write -> startWrite(clientId, clientHost, write.getValue());
            case ReadOperation read -> startRead(clientId, clientHost);
            default -> throw new IllegalOperationException(op);
        };
    }

    public String startWrite(final String clientId, final Host clientHost, final byte[] value) {
        final String opId = createOpId();
        ops.put(opId, new WriteOperation(opId, clientId, clientHost, value));
        return opId;
    }

    public String startRead(final String clientId, final Host clientHost) {
        final String opId = createOpId();
        ops.put(opId, new ReadOperation(opId, clientId, clientHost));
        return opId;
    }

    public Operation stop(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.remove(opId);
    }

    public WriteOperation stopWrite(final String opId) {
        final Operation op = stop(opId);
        if (op instanceof WriteOperation write) {
            return write;
        }
        throw new IllegalOperationException(op);
    }

    public ReadOperation stopRead(final String opId) {
        final Operation op = stop(opId);
        if (op instanceof ReadOperation read) {
            return read;
        }
        throw new IllegalOperationException(op);
    }

    public Set<ReplicaMessage> clearReceived(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        final Operation op = ops.get(opId);
        final Set<ReplicaMessage> res = Set.copyOf(op.received);
        op.received.clear();
        return res;
    }

    public boolean receiveMessage(final String opId, final ReplicaMessage message) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId).receive(message);
    }

    public boolean hasQuorum(final String opId, final int peersSize) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId).hasQuorum(peersSize);
    }

    public boolean hasOp(final String opId) {
        return ops.containsKey(opId);
    }

    public Operation getOp(final String opId) {
        if (!ops.containsKey(opId)) throw new UnknownOperationException(opId);
        return ops.get(opId);
    }
}
