package jatymon.babelprojects.abp.messages;

import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class AckMessage extends ProtoMessage implements ABPMessage {
    public static final short ID = 401;

    private final byte bit;

    public AckMessage(byte bit) {
        super(ID);
        this.bit = bit;
    }

    @Override
    public byte getBit() {
        return bit;
    }

    @Override
    public String toString() {
        return "AckMessage{bit: %s}".formatted(bit);
    }

    public static ISerializer<AckMessage> serializer = new ISerializer<AckMessage>() {
        @Override
        public void serialize(final AckMessage ackMessage, final ByteBuf byteBuf) throws IOException {
            byteBuf.writeByte(ackMessage.bit);
        }

        @Override
        public AckMessage deserialize(final ByteBuf byteBuf) throws IOException {
            return new AckMessage(byteBuf.readByte());
        }
    };
}
