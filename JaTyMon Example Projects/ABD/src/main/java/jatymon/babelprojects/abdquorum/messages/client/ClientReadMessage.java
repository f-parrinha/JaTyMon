package jatymon.babelprojects.abdquorum.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class ClientReadMessage extends ProtoMessage implements ClientMessage {
    public static final short ID = 502;

    private final String clientId;

    public ClientReadMessage(final String clientId) {
        super(ID);
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientReadMessage other
                && clientId.equals(other.clientId);
    }

    @Override
    public String toString() {
        return "ClientReadMessage{clientId: %s}".formatted(clientId);
    }

    public static ISerializer<ClientReadMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientReadMessage clientReadMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, clientReadMessage.clientId);
        }

        @Override
        public ClientReadMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            return new ClientReadMessage(clientId);
        }
    };
}
