package jatymon.babelprojects.multipaxos.messages.replica;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.messages.tags.BallotMessage;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;
import java.util.HexFormat;

public class DecidedMessage extends ReplicaMessage implements BallotMessage {
    public static final short ID = 507;

    private final int ballot;
    private final byte[] value;

    public DecidedMessage(final String opId, final int ballot, final byte[] value) {
        super(ID, opId);
        this.ballot = ballot;
        this.value = value;
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

    @Override
    public String toString() {
        return "DecidedMessage{opId: %s, ballot: %s, value: %s}".formatted(opId, ballot, getValueHex());
    }

    public static ISerializer<DecidedMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final DecidedMessage message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.opId);
            byteBuf.writeInt(message.ballot);
            SerializeUtils.serializeBytes(byteBuf, message.value);
        }

        @Override
        public DecidedMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final int ballot = byteBuf.readInt();
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);
            return new DecidedMessage(opId, ballot, value);
        }
    };
}
