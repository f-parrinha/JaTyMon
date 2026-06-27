package jatymon.babelprojects.abp.messages;

import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class BitMessage extends ProtoMessage implements ABPMessage {
    public static final short ID = 402;

    private final byte bit;

    public BitMessage(byte bit) {
        super(ID);
        this.bit = bit;
    }

    @Override
    public byte getBit() {
        return  bit;
    }

    @Override
    public String toString() {
        return "BitMessage{bit: %s}".formatted(bit);
    }

    public static ISerializer<BitMessage> serializer = new ISerializer<BitMessage>() {
        @Override
        public void serialize(final BitMessage bitMessage, final ByteBuf byteBuf) throws IOException {
            byteBuf.writeByte(bitMessage.bit);
        }

        @Override
        public BitMessage deserialize(final ByteBuf byteBuf) throws IOException {
            return new BitMessage(byteBuf.readByte());
        }
    };
}
