package jatymon.babelprojects.multipaxos.messages.client;

import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.data.Host;

public abstract class ClientMessage extends ProtoMessage {
    protected final Host host;
    protected final String clientId;

    public ClientMessage(final short id, final String clientId, final Host host) {
        super(id);
        this.host = host;
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public Host getHost() {
        return host;
    }
}