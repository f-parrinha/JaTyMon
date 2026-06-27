package jatymon.babelprojects.abdquorum.messages.replica;

import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;

public abstract class ReplicaMessage extends ProtoMessage {
    protected final String opId;

    public ReplicaMessage(final short id, final String opId) {
        super(id);
        this.opId = opId;
    }

    public String getOpId() {
        return opId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReplicaMessage other && opId.equals(other.opId);
    }
}
