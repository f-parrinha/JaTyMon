package perdiagnostic.enumeratealldecisions;

import jatymon.annotations.Typestate;
import perdiagnostic.common.TestEnum;

@Typestate("./EnumerateAllDecisions.protocol")
public class EnumerateAllDecisions {
    public TestEnum m() {
        return TestEnum.TEST_1;
    }
}