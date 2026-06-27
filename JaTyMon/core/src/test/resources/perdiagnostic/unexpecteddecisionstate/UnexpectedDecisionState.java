package perdiagnostic.unexpecteddecisionstate;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestClass;

@Typestate("UnexpectedDecisionState")
public class UnexpectedDecisionState {
    public int m1() {
        return 0;
    }
    public TestClass m2() {
        return null;
    }
}