package jatymon.babelprojects.abdquorum.requests;

import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.babel.generic.ProtoRequest;
import pt.unl.fct.di.novasys.network.data.Host;

public class SendMessageRequest extends ProtoRequest {
    public static final short ID = 203;

    private final Host destination;
    private final ProtoMessage message;

    public SendMessageRequest(final ProtoMessage message, final Host destination) {
        super(ID);
        this.destination = destination;
        this.message = message;
    }

    public ProtoMessage getMessage() {
        return message;
    }
    public Host getDestination() {
        return destination;
    }
}
