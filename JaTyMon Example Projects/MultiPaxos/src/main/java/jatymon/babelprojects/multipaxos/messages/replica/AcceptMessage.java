package jatymon.babelprojects.multipaxos.messages.replica;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.messages.tags.BallotMessage;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;
import java.util.HexFormat;

public class AcceptMessage extends ReplicaMessage implements BallotMessage {
    public static final short ID = 505;

    private final int slot;
    private final int ballot;
    private final boolean noop;
    private final byte[] value;

    public AcceptMessage(final String opId, final int slot, final int ballot, final byte[] value, final boolean noop) {
        super(ID, opId);
        this.slot = slot;
        this.ballot = ballot;
        this.value = value;
        this.noop = noop;
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

    public String getValueHex() {
        return HexFormat.of().formatHex(value);
    }


    public boolean isNoop() {
        return noop;
    }

    @Override
    public String toString() {
        return "AcceptMessage{opId: %s, slot: %s, ballot: %s, value: %s, noop: %s}".formatted(opId, slot, ballot, getValueHex(), noop);
    }

    public static ISerializer<AcceptMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final AcceptMessage message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.opId);
            byteBuf.writeInt(message.slot);
            byteBuf.writeInt(message.ballot);
            byteBuf.writeBoolean(message.noop);
            SerializeUtils.serializeBytes(byteBuf, message.value);
        }

        @Override
        public AcceptMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final int slot = byteBuf.readInt();
            final int ballot = byteBuf.readInt();
            final boolean noop = byteBuf.readBoolean();
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);
            return new AcceptMessage(opId, slot, ballot, value, noop);
        }
    };
}
