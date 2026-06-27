package internalstate;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("BadInternalState")
public class BadInternalState {
    @Ext
    protected int n;
    public void m() {
        // Nothing to do here...
    }
}