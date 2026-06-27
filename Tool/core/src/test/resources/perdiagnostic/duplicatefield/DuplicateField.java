package perdiagnostic.duplicatefield;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("DuplicateField")
public class DuplicateField {
    @Ext
    protected int t;
    @Ext
    protected int y;

    public void m() {
        // Nothing to do here...
    }
}