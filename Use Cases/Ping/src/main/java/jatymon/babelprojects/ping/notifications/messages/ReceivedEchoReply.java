package jatymon.babelprojects.ping.notifications.messages;

import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReceivedEchoReply extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 602;

    private final EchoReplyMessage message;
    private final Host sender;

    public ReceivedEchoReply(final EchoReplyMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    public EchoReplyMessage getMessage() {
        return message;
    }

    public Host getSender() {
        return sender;
    }
}
