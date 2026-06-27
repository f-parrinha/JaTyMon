package jatymon.babelprojects.abdquorum.messages.replica.writeop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ReadTagMessage extends ReplicaMessage implements ClientMessage {
    public static final short ID = 510;

    private final String clientId;

    public ReadTagMessage(final String opId, final String clientId) {
        super(ID, opId);
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReadTagMessage other && clientId.equals(other.clientId);
    }

    @Override
    public String toString() {
        return "ReadTagMessage{opId: %s, clientId: %s}".formatted(opId, clientId);
    }

    public static ISerializer<ReadTagMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ReadTagMessage readTagMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, readTagMessage.opId);
            SerializeUtils.serializeString(byteBuf, readTagMessage.clientId);
        }

        @Override
        public ReadTagMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final String clientId = SerializeUtils.deserializeString(byteBuf);

            return new ReadTagMessage(opId, clientId);
        }
    };
}
