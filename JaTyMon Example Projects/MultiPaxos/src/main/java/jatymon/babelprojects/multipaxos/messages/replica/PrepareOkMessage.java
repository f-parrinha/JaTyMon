package jatymon.babelprojects.multipaxos.messages.replica;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.messages.tags.BallotMessage;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;
import java.util.HexFormat;

public class PrepareOkMessage extends ReplicaMessage implements BallotMessage {
    public static final short ID = 510;
    public static final String NULL_BYTES = "(null)";

    private final int ballot;
    private final int highestAccepted;
    private final byte[] value;

    public PrepareOkMessage(final String opId, final int ballot, final int highestAccepted, final byte[] value) {
        super(ID, opId);
        this.ballot = ballot;
        this.highestAccepted = highestAccepted;
        this.value = value;
    }

    public int getBallot() {
        return ballot;
    }

    public int getHighestAccepted() {
        return highestAccepted;
    }

    public byte[] getValue() {
        return value;
    }

    public String getValueHex() {
        return value == null ? NULL_BYTES : HexFormat.of().formatHex(value);
    }

    @Override
    public String toString() {
        return "PrepareOkMessage{opId: %s, ballot: %s, highestAccept: %s, value: %s}".formatted(opId, ballot, highestAccepted, getValueHex());
    }

    public static ISerializer<PrepareOkMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final PrepareOkMessage message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.opId);
            byteBuf.writeInt(message.ballot);
            byteBuf.writeInt(message.highestAccepted);
            SerializeUtils.serializeBytes(byteBuf, message.value);
        }

        @Override
        public PrepareOkMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final int ballot = byteBuf.readInt();
            final int highestAccept = byteBuf.readInt();
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);
            return new PrepareOkMessage(opId, ballot, highestAccept, value);
        }
    };
}
