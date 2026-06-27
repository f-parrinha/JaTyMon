package jatymon.babelprojects.abdquorum.notifications.messages.readop;

import jatymon.babelprojects.abdquorum.messages.replica.readop.ReadAck;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReadAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 407;

    private final ReadAck message;
    private final Host sender;

    public ReadAckNotification(final ReadAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ReadAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
