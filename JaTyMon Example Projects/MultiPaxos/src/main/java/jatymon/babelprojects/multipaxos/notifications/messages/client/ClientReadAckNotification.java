package jatymon.babelprojects.multipaxos.notifications.messages.client;

import jatymon.babelprojects.multipaxos.messages.client.ClientReadAck;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ClientReadAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 403;

    private final ClientReadAck message;
    private final Host sender;

    public ClientReadAckNotification(final ClientReadAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ClientReadAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
