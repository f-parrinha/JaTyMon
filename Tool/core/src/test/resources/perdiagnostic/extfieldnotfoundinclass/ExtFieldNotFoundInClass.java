package perdiagnostic.extfieldnotfoundinclass;

import jatymon.annotations.Typestate;

@Typestate("ExtFieldNotFoundInClass")
public class ExtFieldNotFoundInClass {

    // Not gonna be found
    int n;

    // Not gonna be found
    private int y;

    // Not gonna be found
    private static int z;

    public void m() {
        // Nothing to do here
    }
}