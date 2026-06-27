package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.AcceptOkMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class AcceptOkMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 408;

    private final AcceptOkMessage message;
    private final Host sender;

    public AcceptOkMessageNotification(final AcceptOkMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public AcceptOkMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
