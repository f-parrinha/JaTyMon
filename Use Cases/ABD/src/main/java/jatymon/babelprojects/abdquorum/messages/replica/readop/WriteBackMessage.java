package jatymon.babelprojects.abdquorum.messages.replica.readop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import jatymon.babelprojects.abdquorum.messages.tags.EntryMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class WriteBackMessage extends ReplicaMessage implements ClientMessage, EntryMessage {
    public static final short ID = 508;

    private final String clientId;
    private final Database.Entry entry;

    public WriteBackMessage(final String opId, final String clientId, final Database.Entry entry) {
        super(ID, opId);
        this.clientId = clientId;
        this.entry = entry;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof WriteBackMessage other
                && clientId.equals(other.clientId)
                && entry.equals(other.entry);
    }

    @Override
    public Database.Entry getEntry() {
        return entry;
    }

    @Override
    public String toString() {
        return "WriteBackMessage{opId: %s, clientId: %s, entry: %s}".formatted(opId, clientId, entry);
    }

    public static ISerializer<WriteBackMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final WriteBackMessage writeBackMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, writeBackMessage.opId);
            SerializeUtils.serializeString(byteBuf, writeBackMessage.clientId);
            SerializeUtils.serializeTag(byteBuf, writeBackMessage.entry.tag());
            SerializeUtils.serializeBytes(byteBuf, writeBackMessage.entry.value());
        }

        @Override
        public WriteBackMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            final Tag tag = SerializeUtils.deserializeTag(byteBuf);
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);

            final Database.Entry entry = new Database.Entry(tag, value);
            return new WriteBackMessage(opId, clientId, entry);
        }
    };
}
