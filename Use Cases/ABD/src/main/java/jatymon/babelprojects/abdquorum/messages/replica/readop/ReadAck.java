package jatymon.babelprojects.abdquorum.messages.replica.readop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.EntryMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ReadAck extends ReplicaMessage implements EntryMessage {
    public static final short ID = 505;

    private final Database.Entry entry;

    public ReadAck(final String opId, final Database.Entry entry) {
        super(ID, opId);
        this.entry = entry;
    }

    @Override
    public String getOpId() {
        return super.getOpId();
    }

    @Override
    public Database.Entry getEntry() {
        return entry;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReadAck other
                && entry.equals(other.entry);
    }

    @Override
    public String toString() {
        return "ReadAck{opId: %s, entry: %s}".formatted(opId, entry);
    }


    public static ISerializer<ReadAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ReadAck readAck, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, readAck.opId);
            SerializeUtils.serializeTag(byteBuf, readAck.entry.tag());
            SerializeUtils.serializeBytes(byteBuf, readAck.entry.value());
        }

        @Override
        public ReadAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final Tag tag = SerializeUtils.deserializeTag(byteBuf);
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);

            final Database.Entry entry = new Database.Entry(tag, value);
            return new ReadAck(opId, entry);
        }
    };
}
