package jatymon.babelprojects.ping.notifications.messages;

import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReceivedEchoRequest extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 603;

    private final EchoRequestMessage message;
    private final Host sender;

    public ReceivedEchoRequest(final EchoRequestMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    public EchoRequestMessage getMessage() {
        return message;
    }

    public Host getSender() {
        return sender;
    }
}
