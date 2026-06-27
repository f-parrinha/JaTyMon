package perdiagnostic.expecteddecisionstate;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestEnum;

@Typestate("ExpectedDecisionState")
public class ExpectedDecisionState {
    public TestEnum m() {
        return TestEnum.TEST_1;
    }
}