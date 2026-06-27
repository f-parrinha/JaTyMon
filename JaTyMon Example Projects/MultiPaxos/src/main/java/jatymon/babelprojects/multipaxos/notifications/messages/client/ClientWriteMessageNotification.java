package jatymon.babelprojects.multipaxos.notifications.messages.client;

import jatymon.babelprojects.multipaxos.messages.client.ClientWriteMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ClientWriteMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 406;

    private final ClientWriteMessage message;
    private final Host sender;

    public ClientWriteMessageNotification(final ClientWriteMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ClientWriteMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
