package jatymon.babelprojects.abp.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class ConnectionFailureTimer extends ProtoTimer {
    public static final short ID = 302;

    public ConnectionFailureTimer() {
        super(ID);
    }

    @Override
    public ProtoTimer clone() {
        return new ConnectionFailureTimer();
    }
}
