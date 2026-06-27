package jatymon.babelprojects.ping.messages;

import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class EchoRequestMessage extends ProtoMessage {
    public static final short ID = 202;

    public EchoRequestMessage() {
        super(ID);
    }

    @Override
    public String toString() {
        return "EchoRequest";
    }

    public static ISerializer<EchoRequestMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(EchoRequestMessage msg, ByteBuf byteBuf) throws IOException {
            // Nothing to do here
        }

        @Override
        public EchoRequestMessage deserialize(ByteBuf byteBuf) throws IOException {
            return new EchoRequestMessage();
        }
    };
}
