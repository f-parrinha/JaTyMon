package jatymon.babelprojects.abdquorum.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;

public class ClientReadAck extends ProtoMessage implements ClientMessage {
    public static final short ID = 501;

    private final String clientId;
    private final byte[] value;

    public ClientReadAck(final String clientId, final byte[] value) {
        super(ID);
        this.clientId = clientId;
        this.value = value;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ClientReadAck other
                && clientId.equals(other.clientId)
                && Arrays.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "ClientReadAck{clientId: %s, value: %s}".formatted(clientId, HexFormat.of().formatHex(value));
    }

    public static ISerializer<ClientReadAck> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientReadAck clientReadAck, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, clientReadAck.clientId);

            byteBuf.writeInt(clientReadAck.value.length);
            byteBuf.writeBytes(clientReadAck.value);
        }

        @Override
        public ClientReadAck deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);

            final int valueLen = byteBuf.readInt();
            final byte[] value = new byte[valueLen];
            byteBuf.readBytes(value);
            return new ClientReadAck(clientId, value);
        }
    };
}
