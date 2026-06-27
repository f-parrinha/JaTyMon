package key;

import jatymon.annotations.Typestate;

@Typestate("KeyBasic3")
public class KeyBasic3 {

    // No Key annotation should make semantic validation fail!
    protected String clientId;

    public void m() {
        // Nothing to do here...
    }
}