package jatymon.babelprojects.ping.notifications.messages;

import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.data.Host;

public interface ReceivedMessageNotification {
    ProtoMessage getMessage();
    Host getSender();
}
