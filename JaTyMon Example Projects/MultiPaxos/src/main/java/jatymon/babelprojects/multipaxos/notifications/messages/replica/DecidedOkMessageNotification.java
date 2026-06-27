package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.DecidedOkMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class DecidedOkMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 410;

    private final DecidedOkMessage message;
    private final Host sender;

    public DecidedOkMessageNotification(final DecidedOkMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public DecidedOkMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
