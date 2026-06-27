package jatymon.babelprojects.abdquorum.messages.replica.readop;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ReadMessage extends ReplicaMessage implements ClientMessage {
    public static final short ID = 506;

    private final String clientId;

    public ReadMessage(final String opId, final String clientId) {
        super(ID, opId);
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReadMessage other
                && clientId.equals(other.clientId);
    }

    @Override
    public String toString() {
        return "ReadMessage{opId: %s, clientId: %s}".formatted(opId, clientId);
    }

    public static ISerializer<ReadMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ReadMessage readMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, readMessage.opId);
            SerializeUtils.serializeString(byteBuf, readMessage.clientId);
        }

        @Override
        public ReadMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String opId = SerializeUtils.deserializeString(byteBuf);
            final String clientId = SerializeUtils.deserializeString(byteBuf);

            return new ReadMessage(opId, clientId);
        }
    };
}
