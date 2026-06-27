package jatymon.babelprojects.multipaxos.session;

import jatymon.babelprojects.multipaxos.exceptions.IllegalMessageException;
import jatymon.babelprojects.multipaxos.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.util.*;

public class Operation {
    protected final String id;
    protected final Type type;
    protected final String clientId;
    protected final Host clientHost;
    protected final Set<ReplicaMessage> received;
    protected final int slot;

    protected long retryTimerId;
    protected int ballot;
    protected byte[] value;

    protected Operation(final String id,
                        final String clientId,
                        final Host clientHost,
                        final int slot,
                        final int ballot,
                        final byte[] value,
                        final long retryTimerId,
                        final Type type) {
        this.id = id;
        this.type = type;
        this.clientId = clientId;
        this.clientHost = clientHost;
        this.received = new HashSet<>();
        this.slot = slot;
        this.value = value;
        this.ballot = ballot;
        this.retryTimerId = retryTimerId;
    }

    protected Operation(final String id,
                        final String clientId,
                        final Host clientHost,
                        final int slot,
                        final int ballot) {
        this(id, clientId, clientHost, slot, ballot, null, -1 ,Type.READ);
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

    public String getId() {
        return id;
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

    public int getSlot() {
        return slot;
    }

    public int getBallot() {
        return ballot;
    }

    public byte[] getValue() {
        return value;
    }

    public String getProposerValHex() {
        return HexFormat.of().formatHex(value);
    }

    public long getRetryTimerId() {
        return retryTimerId;
    }

    public void setBallot(final int proposerSeq) {
        this.ballot = proposerSeq;
    }

    public void setValue(final byte[] proposerVal) {
        this.value = proposerVal;
    }

    public void setRetryTimerId(long retryTimerId) {
        this.retryTimerId = retryTimerId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Operation other && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientHost, received, slot, ballot, Arrays.hashCode(value), type);
    }

    @Override
    public String toString() {
        return "Operation{opId: %s, clientId: %s, clientHost: %s, received: %s, ballot: %s, value: %s}".formatted(
                id, clientId, clientHost, received, ballot, getProposerValHex());
    }

    public enum Type { READ, WRITE }

    public static final class Builder {
        private long retryTimerId;
        private int slot;
        private int ballot;
        private byte[] value;
        private Type type;
        private String opId;
        private String clientId;
        private Host clientHost;

        public Builder() {
            this.slot = 0;
            this.ballot = 0;
            this.value = new byte[0];
            this.opId = null;
            this.type = null;
            this.clientId = null;
            this.clientHost = null;
        }

        public Builder withSlot(final int slot) {
            this.slot = slot;
            return this;
        }

        public Builder withBallot(final int ballot) {
            this.ballot = ballot;
            return this;
        }

        public Builder withValue(final byte[] value) {
            this.value = value;
            return this;
        }

        public Builder withOpId(final String opId) {
            this.opId = opId;
            return this;
        }

        public Builder withClientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withClientHost(final Host clientHost) {
            this.clientHost = clientHost;
            return this;
        }

        public Builder withType(final Type type) {
            this.type = type;
            return this;
        }

        public Builder withRetryTimerId(final long retryTimerId) {
            this.retryTimerId = retryTimerId;
            return this;
        }

        public Operation build() {
            return new Operation(opId, clientId, clientHost, slot, ballot, value, retryTimerId, type);
        }
    }
}
