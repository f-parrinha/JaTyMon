package jatymon.babelprojects.ping.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class FailureTimer extends ProtoTimer {
    public static final short ID = 302;

    public FailureTimer() {
        super(ID);
    }

    @Override
    public ProtoTimer clone() {
        return new FailureTimer();
    }
}
