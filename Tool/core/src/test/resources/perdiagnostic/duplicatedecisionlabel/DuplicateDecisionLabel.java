package perdiagnostic.duplicatedecisionlabel;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestEnum;

@Typestate("DuplicateDecisionLabel")
public class DuplicateDecisionLabel {
    public TestEnum m() {
        return TestEnum.TEST_1;
    }
}