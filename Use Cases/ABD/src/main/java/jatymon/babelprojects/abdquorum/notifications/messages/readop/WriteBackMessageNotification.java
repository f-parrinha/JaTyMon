package jatymon.babelprojects.abdquorum.notifications.messages.readop;

import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackMessage;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class WriteBackMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 410;

    private final WriteBackMessage message;
    private final Host sender;

    public WriteBackMessageNotification(final WriteBackMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public WriteBackMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
