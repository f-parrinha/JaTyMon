package jatymon.babelprojects.abp.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class SendBitTimer extends ProtoTimer {
    public static final short ID = 303;
    public static final int TIMEOUT = 1000;

    public SendBitTimer() {
        super(ID);
    }

    @Override
    public ProtoTimer clone() {
        return new SendBitTimer();
    }
}
