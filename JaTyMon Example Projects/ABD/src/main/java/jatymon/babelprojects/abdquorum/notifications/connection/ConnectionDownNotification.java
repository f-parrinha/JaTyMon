package jatymon.babelprojects.abdquorum.notifications.connection;

import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ConnectionDownNotification extends ProtoNotification implements ConnectionNotification{
    public static final short ID = 401;

    private final Host host;
    private final int connections;
    private final int channelId;

    public ConnectionDownNotification(final Host host, final int connections, final int channelId) {
        super(ID);
        this.host = host;
        this.connections = connections;
        this.channelId = channelId;
    }

    public Host getHost() {
        return host;
    }

    public int getConnections() {
        return connections;
    }

    public int getChannelId() {
        return channelId;
    }
}
