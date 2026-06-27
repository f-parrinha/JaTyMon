package jatymon.babelprojects.ping.messages;

import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class EchoReplyMessage extends ProtoMessage {
    public static final short ID = 201;

    public EchoReplyMessage() {
        super(ID);
    }

    @Override
    public String toString() {
        return "EchoReply";
    }

    public static ISerializer<EchoReplyMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(EchoReplyMessage msg, ByteBuf byteBuf) throws IOException {
            // Nothing to do here
        }

        @Override
        public EchoReplyMessage deserialize(ByteBuf byteBuf) throws IOException {
            return new EchoReplyMessage();
        }
    };
}
