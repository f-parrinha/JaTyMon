package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.PrepareMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class PrepareMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 411;

    private final PrepareMessage message;
    private final Host sender;

    public PrepareMessageNotification(final PrepareMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public PrepareMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
