package jatymon.babelprojects.abdquorum.notifications.messages.writeop;

import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteMessage;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class WriteMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 414;

    private final WriteMessage message;
    private final Host sender;

    public WriteMessageNotification(final WriteMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public WriteMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
