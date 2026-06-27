package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.DecidedMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class DecidedMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 409;

    private final DecidedMessage message;
    private final Host sender;

    public DecidedMessageNotification(final DecidedMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public DecidedMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
