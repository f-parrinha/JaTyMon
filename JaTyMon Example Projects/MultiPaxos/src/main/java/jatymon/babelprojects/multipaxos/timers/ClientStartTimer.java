package jatymon.babelprojects.multipaxos.timers;

import pt.unl.fct.di.novasys.babel.generic.ProtoTimer;

public class ClientStartTimer extends ProtoTimer {
    public static final short ID = 302;

    public ClientStartTimer() {
        super(ID);
    }

    @Override
    public ProtoTimer clone() {
        return new ClientStartTimer();
    }
}
