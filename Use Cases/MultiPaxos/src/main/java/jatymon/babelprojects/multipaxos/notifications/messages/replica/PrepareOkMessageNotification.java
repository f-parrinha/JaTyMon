package jatymon.babelprojects.multipaxos.notifications.messages.replica;

import jatymon.babelprojects.multipaxos.messages.replica.PrepareOkMessage;
import jatymon.babelprojects.multipaxos.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class PrepareOkMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 412;

    private final PrepareOkMessage message;
    private final Host sender;

    public PrepareOkMessageNotification(final PrepareOkMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public PrepareOkMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
