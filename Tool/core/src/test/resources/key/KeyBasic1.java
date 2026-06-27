package key;

import jatymon.annotations.Key;
import jatymon.annotations.Typestate;

@Typestate("KeyBasic1")
public class KeyBasic1 {

    @Key
    protected String client;

    public void m() {
        // Nothing to do here...
    }
}