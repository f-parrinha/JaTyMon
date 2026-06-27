package jatymon.babelprojects.abp.notifications.messages;

import jatymon.babelprojects.abp.messages.BitMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReceivedBitMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 501;

    private final BitMessage message;
    private final Host sender;

    public ReceivedBitMessageNotification(final BitMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public BitMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
