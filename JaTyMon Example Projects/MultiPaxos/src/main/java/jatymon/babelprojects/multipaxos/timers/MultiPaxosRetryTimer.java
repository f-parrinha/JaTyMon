package jatymon.babelprojects.multipaxos.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class MultiPaxosRetryTimer extends ProtoTimer {
    public static final short ID = 305;
    public static final long TIMEOUT = 3000;

    private final String opId;

    public MultiPaxosRetryTimer(final String opId) {
        super(ID);
        this.opId = opId;
    }

    public String getOpId() {
        return opId;
    }

    @Override
    public ProtoTimer clone() {
        return new MultiPaxosRetryTimer(opId);
    }
}
