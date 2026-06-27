package jatymon.babelprojects.abdquorum.operations;

import jatymon.babelprojects.abdquorum.exceptions.IllegalMessageException;
import jatymon.babelprojects.abdquorum.exceptions.IllegalOperationException;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Operation {
    protected final Type type;
    protected final String opId;
    protected final String clientId;
    protected final Host clientHost;
    protected final Set<ReplicaMessage> received;

    public Operation(final Type type, final String opId, final String clientId, final Host clientHost) {
        this.type = type;
        this.opId = opId;
        this.clientId = clientId;
        this.clientHost = clientHost;
        this.received = new HashSet<>();
    }

    public boolean receive(final ReplicaMessage message) {
        if (received.isEmpty()) {
            received.add(message);
            return true;
        }

        // Make sure all messages in the set are of the same type at once
        final ReplicaMessage first = received.iterator().next();
        if (!message.getClass().equals(first.getClass())) {
            throw new IllegalMessageException(message);
        }

        return received.add(message);
    }

    public boolean hasQuorum(final int peersSize) {
        final int acks = received.size();
        return acks > peersSize / 2;
    }

    public Type getType() {
        return type;
    }

    public String getClientId() {
        return clientId;
    }

    public Host getClientHost() {
        return clientHost;
    }

    public Set<ReplicaMessage> getReceived() {
        return Collections.unmodifiableSet(received);
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Operation other
                && type.equals(other.type)
                && clientId.equals(other.clientId)
                && received.equals(other.received);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, clientId, clientHost, received);
    }

    @Override
    public String toString() {
        return "Operation{opId: %s, type: %s, clientId: %s, clientHost: %s, received: %s}".formatted(opId, type, clientId, clientHost, received);
    }

    public enum Type {
        READ,
        WRITE;

        public static Type fromOp(final Operation op) {
            return switch (op) {
                case ReadOperation r -> READ;
                case WriteOperation w -> WRITE;
                default -> throw new IllegalOperationException(op);
            };
        }
    }
}
