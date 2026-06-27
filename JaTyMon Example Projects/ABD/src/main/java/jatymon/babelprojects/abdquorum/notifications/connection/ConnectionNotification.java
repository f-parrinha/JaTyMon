package jatymon.babelprojects.abdquorum.notifications.connection;

import pt.unl.fct.di.novasys.network.data.Host;

public interface ConnectionNotification {
    Host getHost();
    int getConnections();
}
