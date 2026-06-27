package jatymon.babelprojects.abdquorum.timers;

import jatymon.babelprojects.abdquorum.protocols.client.ClientProtocol;
import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class ClientRetryTimer extends ProtoTimer {
    public static final short ID = 301;
    public static final long TIMEOUT = 1000;

    private final ClientProtocol.Op op;

    public ClientRetryTimer(final ClientProtocol.Op op) {
        super(ID);
        this.op = op;
    }

    public ClientProtocol.Op getOp() {
        return op;
    }

    @Override
    public ProtoTimer clone() {
        return new ClientRetryTimer(op);
    }
}
