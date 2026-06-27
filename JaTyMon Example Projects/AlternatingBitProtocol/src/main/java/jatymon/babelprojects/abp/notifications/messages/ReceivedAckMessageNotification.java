package jatymon.babelprojects.abp.notifications.messages;

import jatymon.babelprojects.abp.messages.AckMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReceivedAckMessageNotification extends ProtoNotification implements ReceivedMessageNotification{
    public static final short ID = 500;

    private final AckMessage message;
    private final Host sender;

    public ReceivedAckMessageNotification(final AckMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public AckMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
