package jatymon.babelprojects.abp.notifications.connection;

import pt.unl.fct.di.novasys.network.data.Host;

public interface ConnectionNotification {
    Host getHost();
    int getConnections();
}
