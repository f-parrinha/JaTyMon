package jatymon.babelprojects.multipaxos.notifications.messages.client;

import jatymon.babelprojects.multipaxos.messages.client.ClientReadMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ClientReadMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 404;

    private final ClientReadMessage message;
    private final Host sender;

    public ClientReadMessageNotification(final ClientReadMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ClientReadMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
