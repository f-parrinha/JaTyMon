package jatymon.babelprojects.multipaxos.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.multipaxos.util.SerializeUtils;
import pt.unl.fct.di.novasys.network.ISerializer;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;

public class ClientReadAck extends ClientMessage {
    public static final short ID = 501;

    private final byte[] value;

    public ClientReadAck(final String clientId, final Host host, final byte[] value) {
        super(ID, clientId, host);
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientReadAck other && Arrays.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "ClientReadAck{clientId: %s, host: %s, value: %s}".formatted(clientId, host, HexFormat.of().formatHex(value));
    }

    public static ISerializer<ClientReadAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientReadAck message, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, message.clientId);
            SerializeUtils.serializeHost(byteBuf, message.host);
            SerializeUtils.serializeBytes(byteBuf, message.value);
        }

        @Override
        public ClientReadAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);
            final Host host = SerializeUtils.deserializeHost(byteBuf);
            final byte[] value = SerializeUtils.deserializeBytes(byteBuf);
            return new ClientReadAck(clientId, host, value);
        }
    };
}
