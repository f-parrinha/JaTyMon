package jatymon.babelprojects.abdquorum.notifications.messages.writeop;

import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteAck;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class WriteAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 413;

    private final WriteAck message;
    private final Host sender;

    public WriteAckNotification(final WriteAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public WriteAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
