package jatymon.babelprojects.multipaxos.timers;

import jatymon.babelprojects.multipaxos.session.Operation;
import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class ClientRetryTimer extends ProtoTimer {
    public static final short ID = 301;
    public static final long TIMEOUT = 10_000;

    private final Operation.Type op;

    public ClientRetryTimer(final Operation.Type op) {
        super(ID);
        this.op = op;
    }

    public Operation.Type getOp() {
        return op;
    }

    @Override
    public ProtoTimer clone() {
        return new ClientRetryTimer(op);
    }
}
