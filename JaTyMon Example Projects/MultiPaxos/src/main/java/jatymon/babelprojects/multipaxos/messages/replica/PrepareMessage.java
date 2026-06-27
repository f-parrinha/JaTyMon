package jatymon.babelprojects.multipaxos.messages.replica;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.messages.tags.BallotMessage;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class PrepareMessage extends ReplicaMessage implements BallotMessage {
    public static final short ID = 509;

    private final int slot;
    private final int ballot;

    public PrepareMessage(final String opId, final int slot, final int ballot) {
        super(ID, opId);
        this.slot = slot;
        this.ballot = ballot;
    }

    public int getBallot() {
        return ballot;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof PrepareMessage;
    }

    @Override
    public String toString() {
        return "PrepareMessage{opId: %s, slot: %s, ballot: %s}".formatted(opId, slot, ballot);
    }

    public static ISerializer<PrepareMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final PrepareMessage message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.opId);
            byteBuf.writeInt(message.slot);
            byteBuf.writeInt(message.ballot);
        }

        @Override
        public PrepareMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final int slot = byteBuf.readInt();
            final int ballot = byteBuf.readInt();
            return new PrepareMessage(opId, slot, ballot);
        }
    };
}
