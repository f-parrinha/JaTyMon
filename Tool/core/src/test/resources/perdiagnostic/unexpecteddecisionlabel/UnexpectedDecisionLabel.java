package perdiagnostic.unexpecteddecisionlabel;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestEnum;

@Typestate("UnexpectedDecisionLabel")
public class UnexpectedDecisionLabel {
    public TestEnum m() {
        return TestEnum.TEST_1;
    }
}