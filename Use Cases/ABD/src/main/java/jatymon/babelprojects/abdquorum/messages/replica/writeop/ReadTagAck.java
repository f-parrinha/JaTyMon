package jatymon.babelprojects.abdquorum.messages.replica.writeop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import jatymon.babelprojects.abdquorum.messages.tags.TagMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ReadTagAck extends ReplicaMessage implements TagMessage {
    public static final short ID = 509;

    private final Tag tag;

    public ReadTagAck(final String opId, final Tag tag) {
        super(ID, opId);
        this.tag = tag;
    }

    @Override
    public Tag getTag() {
        return tag;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReadTagAck other
                && tag.equals(other.tag);
    }

    @Override
    public String toString() {
        return "ReadTagAck{opId: %s, tag: %s}".formatted(opId, tag);
    }

    public static ISerializer<ReadTagAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ReadTagAck writeMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, writeMessage.opId);
            SerializeUtils.serializeTag(byteBuf, writeMessage.tag);
        }

        @Override
        public ReadTagAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final Tag tag = SerializeUtils.deserializeTag(byteBuf);

            return new ReadTagAck(opId, tag);
        }
    };
}
