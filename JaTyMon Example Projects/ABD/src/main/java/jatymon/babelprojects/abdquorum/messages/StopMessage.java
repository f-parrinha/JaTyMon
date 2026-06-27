package jatymon.babelprojects.abdquorum.messages;

import io.netty.buffer.ByteBuf;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.ISerializer;

import java.io.IOException;

public class StopMessage extends ProtoMessage {
    public static final short ID = 513;

    public StopMessage() {
        super(ID);
    }

    public static ISerializer<StopMessage> serializer = new ISerializer<>() {
        @Override
        public void serialize(final StopMessage writeMessage, final ByteBuf byteBuf) throws IOException {
        }

        @Override
        public StopMessage deserialize(final ByteBuf byteBuf) throws IOException {
            return new StopMessage();
        }
    };
}
