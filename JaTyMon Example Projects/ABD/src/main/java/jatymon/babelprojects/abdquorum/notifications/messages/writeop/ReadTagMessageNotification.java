package jatymon.babelprojects.abdquorum.notifications.messages.writeop;

import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagMessage;
import jatymon.babelprojects.abdquorum.notifications.messages.ReceivedMessageNotification;
import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ReadTagMessageNotification extends ProtoNotification implements ReceivedMessageNotification {
    public static final short ID = 412;

    private final ReadTagMessage message;
    private final Host sender;

    public ReadTagMessageNotification(final ReadTagMessage message, final Host sender) {
        super(ID);
        this.message = message;
        this.sender = sender;
    }

    @Override
    public ReadTagMessage getMessage() {
        return message;
    }

    @Override
    public Host getSender() {
        return sender;
    }
}
