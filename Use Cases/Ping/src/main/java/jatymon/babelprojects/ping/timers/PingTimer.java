package jatymon.babelprojects.ping.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class PingTimer extends ProtoTimer {
    public static final short TIMER_ID = 302;

    public PingTimer() {
        super(TIMER_ID);
    }

    @Override
    public ProtoTimer clone() {
        return new PingTimer();
    }
}
