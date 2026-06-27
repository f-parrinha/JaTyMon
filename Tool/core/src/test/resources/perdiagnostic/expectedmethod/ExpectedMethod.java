package perdiagnostic.expectedmethod;

import jatymon.annotations.Typestate;

@Typestate("ExpectedMethod")
public class ExpectedMethod {

    // This one is not public and should not be found by the typestate
    private void m1() {
        // Nothing to do here...
    }

    // The typestate also has the method m2. Don't add it to the class to test if the diagnostic shows up

    // Test different return types
    public int m3() {
        return 0;
    }

    // This one should pass
    protected void m4() {
        // Nothing to do here...
    }
}