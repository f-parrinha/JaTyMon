package jatymon.babelprojects.multipaxos.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;

public class ClientWriteAck extends ClientMessage {
    public static final short ID = 503;

    public ClientWriteAck(final String clientId, final Host host) {
        super(ID, clientId, host);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientWriteAck;
    }

    @Override
    public String toString() {
        return "ClientWriteAck{clientId: %s, host: %s}".formatted(clientId, host);
    }

    public static ISerializer<ClientWriteAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientWriteAck message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.clientId);
            SerializeUtils.serializeHost(byteBuf, message.host);
        }

        @Override
        public ClientWriteAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            final Host host = SerializeUtils.deserializeHost(byteBuf);
            return new ClientWriteAck(clientId, host);
        }
    };
}
