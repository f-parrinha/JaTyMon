package jatymon.typestate.ast.nodes.key;

import jatymon.common.TokenPosition;

public class TNullKey extends TKeyNode {
    public static final String NAME = "NULL-KEY";

    public TNullKey(final TokenPosition tokenPos) {
        super(NAME, tokenPos);
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }
}
