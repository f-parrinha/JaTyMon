package jatymon.babelprojects.abdquorum.operations;

import pt.unl.fct.di.novasys.network.data.Host;

public class ReadOperation extends Operation {
    public ReadOperation(final String opId, final String clientId, final Host clientHost) {
        super(Type.READ, opId, clientId, clientHost);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof ReadOperation;
    }

    @Override
    public String toString() {
        return "ReadOperation{opId: clientId: %s, clientHost: %s, received: %s}".formatted(clientId, clientHost, received);
    }
}
