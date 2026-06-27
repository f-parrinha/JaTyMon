package jatymon.babelprojects.abdquorum.operations;

import pt.unl.fct.di.novasys.network.data.Host;

import java.util.Arrays;

public class WriteOperation extends Operation {
    private final byte[] value;

    public WriteOperation(final String opId, final String clientId, final Host clientHost, final byte[] value) {
        super(Type.WRITE, opId, clientId, clientHost);
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) && obj instanceof WriteOperation other && Arrays.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "WriteOperation{opId: clientId: %s, clientHost: %s, received: %s, value: %s}".formatted(clientId, clientHost, received, value);
    }
}
