package perdiagnostic.extfieldsareimmutable;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;

@Typestate("ExtFieldsAreImmutable")
public class ExtFieldsAreImmutable {

    @Ext
    protected int n;

    public void m() {
        // Nothing to do here...
    }
}