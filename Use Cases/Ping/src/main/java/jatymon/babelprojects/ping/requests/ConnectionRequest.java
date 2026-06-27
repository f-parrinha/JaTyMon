package jatymon.babelprojects.ping.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.novasys.network.data.Host;

public class ConnectionRequest extends ProtoRequest {
    public static final short ID = 501;

    private final Host host;
    private final long delay;

    public ConnectionRequest(final Host host, final long delay) {
        super(ID);
        this.host = host;
        this.delay = Math.abs(delay);
    }

    public ConnectionRequest(final Host host) {
        this(host, 0);
    }

    public Host getHost() {
        return host;
    }

    public long getDelay() {
        return delay;
    }
}
