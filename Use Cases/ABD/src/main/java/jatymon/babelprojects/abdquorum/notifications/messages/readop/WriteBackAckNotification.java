package jatymon.babelprojects.abdquorum.notifications.messages.readop;

import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackAck;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class WriteBackAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 409;

    private final WriteBackAck message;
    private final Host sender;

    public WriteBackAckNotification(final WriteBackAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public WriteBackAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
