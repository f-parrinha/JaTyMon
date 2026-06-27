package jatymon.babelprojects.abdquorum.notifications.messages.writeop;

import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagAck;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReadTagAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 411;

    private final ReadTagAck message;
    private final Host sender;

    public ReadTagAckNotification(final ReadTagAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ReadTagAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
