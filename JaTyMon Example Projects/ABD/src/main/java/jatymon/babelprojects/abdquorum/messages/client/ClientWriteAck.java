package jatymon.babelprojects.abdquorum.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ClientWriteAck extends ProtoMessage implements ClientMessage {
    public static final short ID = 503;

    private final String clientId;

    public ClientWriteAck(final String clientId) {
        super(ID);
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientWriteAck other
                && clientId.equals(other.clientId);
    }

    @Override
    public String toString() {
        return "ClientWriteAck{clientId: %s}".formatted(clientId);
    }

    public static ISerializer<ClientWriteAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientWriteAck clientWriteAck, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, clientWriteAck.clientId);
        }

        @Override
        public ClientWriteAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            return new ClientWriteAck(clientId);
        }
    };
}
