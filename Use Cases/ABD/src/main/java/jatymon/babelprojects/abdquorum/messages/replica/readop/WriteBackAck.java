package jatymon.babelprojects.abdquorum.messages.replica.readop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.EntryMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class WriteBackAck extends ReplicaMessage implements EntryMessage {
    public static final short ID = 507;

    private final Database.Entry entry;

    public WriteBackAck(final String opId, final Database.Entry entry) {
        super(ID, opId);
        this.entry = entry;
    }

    @Override
    public Database.Entry getEntry() {
        return entry;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof WriteBackAck other
                && entry.equals(other.entry);
    }

    @Override
    public String toString() {
        return "WriteBackAck{opId: %s, entry: %s}".formatted(opId, entry);
    }

    public static ISerializer<WriteBackAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final WriteBackAck writeBackAck, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, writeBackAck.opId);
            SerializeUtils.serializeTag(byteBuf, writeBackAck.entry.tag());
            SerializeUtils.serializeBytes(byteBuf, writeBackAck.entry.value());
        }

        @Override
        public WriteBackAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final Tag tag = SerializeUtils.deserializeTag(byteBuf);
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);

            final Database.Entry entry = new Database.Entry(tag, value);
            return new WriteBackAck(opId, entry);
        }
    };
}
