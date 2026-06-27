package key;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("KeyBasic4")
public class KeyBasic4 {

    // Should also fail!!! Must be key
    @Ext
    protected String clientId;

    public void m() {
        // Nothing to do here...
    }
}