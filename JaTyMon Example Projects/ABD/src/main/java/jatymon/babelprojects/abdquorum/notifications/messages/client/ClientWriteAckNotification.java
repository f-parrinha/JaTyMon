package jatymon.babelprojects.abdquorum.notifications.messages.client;

import jatymon.babelprojects.abdquorum.messages.client.ClientWriteAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteMessage;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ClientWriteAckNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 405;

    private final ClientWriteAck message;
    private final Host sender;

    public ClientWriteAckNotification(final ClientWriteAck message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ClientWriteAck getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
