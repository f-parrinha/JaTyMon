package jatymon.babelprojects.multipaxos.notifications.connection;

import pt.unl.fct.di.novasys.babel.generic.ProtoNotification;
import pt.unl.fct.di.novasys.network.data.Host;

public class ConnectionUpNotification extends ProtoNotification implements ConnectionNotification {
    public static final short ID = 402;

    private final Host host;
    private final int connections;
    private final int peerSize;
    private final int channelId;

    public ConnectionUpNotification(final Host host, final int connections, final int peersSize, final int channelId) {
        super(ID);
        this.host = host;
        this.connections = connections;
        this.peerSize = peersSize;
        this.channelId = channelId;
    }

    public Host getHost() {
        return host;
    }

    public int getConnections() {
        return connections;
    }

    public int getPeerSize() {
        return peerSize;
    }

    public int getChannelId() {
        return channelId;
    }
}
