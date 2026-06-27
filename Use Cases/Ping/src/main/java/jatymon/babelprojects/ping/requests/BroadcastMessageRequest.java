package jatymon.babelprojects.ping.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;

public class BroadcastMessageRequest extends ProtoRequest {
    public static final short ID = 500;

    private final ProtoMessage message;

    public BroadcastMessageRequest(final ProtoMessage message) {
        super(ID);
        this.message = message;
    }

    public ProtoMessage getMessage() {
        return message;
    }
}
