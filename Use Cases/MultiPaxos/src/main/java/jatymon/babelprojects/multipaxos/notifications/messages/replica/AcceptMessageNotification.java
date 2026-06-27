package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.AcceptMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class AcceptMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 407;

    private final AcceptMessage message;
    private final Host sender;

    public AcceptMessageNotification(final AcceptMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public AcceptMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
