package jatymon.babelprojects.abdquorum.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class FailureTimer extends ProtoTimer {
    public static final short ID = 303;

    public FailureTimer() {
        super(ID);
    }

    @Override
    public ProtoTimer clone() {
        return new FailureTimer();
    }
}
