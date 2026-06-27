package jatymon.babelprojects.multipaxos.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;

public class ClientReadMessage extends ClientMessage {
    public static final short ID = 502;

    public ClientReadMessage(final String clientId, final Host host) {
        super(ID, clientId, host);
    }


    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientReadMessage;
    }

    @Override
    public String toString() {
        return "ClientReadMessage{clientId: %s, host: %s}".formatted(clientId, host);
    }

    public static ISerializer<ClientReadMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientReadMessage message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.clientId);
            SerializeUtils.serializeHost(byteBuf, message.host);
        }

        @Override
        public ClientReadMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            final Host host = SerializeUtils.deserializeHost(byteBuf);
            return new ClientReadMessage(clientId, host);
        }
    };
}
