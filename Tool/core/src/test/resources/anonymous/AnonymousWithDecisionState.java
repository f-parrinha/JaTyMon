package anonymous;

import jatymon.annotations.Typestate;

@Typestate("AnonymousWithDecisionState")
public class AnonymousWithDecisionState {
    public void m1() {
        // Nothing to do here ...
    }

    public TestEnum m2() {
        return TestEnum.TEST_1;
    }

    public enum TestEnum {
        TEST_1,
        TEST_2,
        TEST_3;
    }
}