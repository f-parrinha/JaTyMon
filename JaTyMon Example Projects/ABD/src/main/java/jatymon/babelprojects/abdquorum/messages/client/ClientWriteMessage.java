package jatymon.babelprojects.abdquorum.messages.client;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.utils.SerializeUtils;
import jatymon.babelprojects.abdquorum.messages.tags.ClientMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;

public class ClientWriteMessage extends ProtoMessage implements ClientMessage {
    public static final short ID = 504;

    private final String clientId;
    private final byte[] value;

    public ClientWriteMessage(final String clientId, final byte[] value) {
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
        return super.equals(obj) && obj instanceof ClientWriteMessage other
                && clientId.equals(other.clientId)
                && Arrays.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "ClientWriteMessage{clientId: %s, value: %s}".formatted(clientId, HexFormat.of().formatHex(value));
    }

    public static ISerializer<ClientWriteMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final ClientWriteMessage clientWriteMessage, final ByteBuf byteBuf) throws IOException {
            SerializeUtils.serializeString(byteBuf, clientWriteMessage.clientId);

            byteBuf.writeInt(clientWriteMessage.value.length);
            byteBuf.writeBytes(clientWriteMessage.value);
        }

        @Override
        public ClientWriteMessage deserialize(final ByteBuf byteBuf) throws IOException {
            final String clientId = SerializeUtils.deserializeString(byteBuf);

            final int valueLen = byteBuf.readInt();
            final byte[] value = new byte[valueLen];
            byteBuf.readBytes(value);
            return new ClientWriteMessage(clientId, value);
        }
    };
}
