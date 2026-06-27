package jatymon.babelprojects.abdquorum.messages.replica.writeop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import jatymon.babelprojects.abdquorum.messages.tags.EntryMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class WriteMessage extends ReplicaMessage implements ClientMessage, EntryMessage {
    public static final short ID = 512;

    private final String clientId;
    private final Database.Entry entry;

    public WriteMessage(final String opId, final String clientId, final Database.Entry entry) {
        super(ID, opId);
        this.clientId = clientId;
        this.entry = entry;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Database.Entry getEntry() {
        return entry;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof WriteMessage other
                && entry.equals(other.entry)
                && clientId.equals(other.clientId);
    }

    @Override
    public String toString() {
        return "WriteMessage{opId: %s, clientId: %s, entry: %s}".formatted(opId, clientId, entry);
    }

    public static ISerializer<WriteMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final WriteMessage writeMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, writeMessage.opId);
            SerializeUtils.serializeString(byteBuf, writeMessage.clientId);
            SerializeUtils.serializeTag(byteBuf, writeMessage.getEntry().tag());
            SerializeUtils.serializeBytes(byteBuf, writeMessage.getEntry().value());
        }

        @Override
        public WriteMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            final Tag tag = SerializeUtils.deserializeTag(byteBuf);
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);

            final Database.Entry entry = new Database.Entry(tag, value);
            return new WriteMessage(opId, clientId, entry);
        }
    };
}
