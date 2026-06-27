import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionsCounterTests {

    private ActionsCounter counter;

    private static final ActionId ACTION_1 = new ActionId("State1", "boot", ActionType.Input);
    private static final ActionId ACTION_2 = new ActionId("State1", "close", ActionType.Input);
    private static final ActionId ACTION_3 = new ActionId("State2", "send", ActionType.Output);

    @BeforeEach
    void setUp() {
        counter = new ActionsCounter();
    }

    @Test
    public void testSample() {
        counter.sample(ACTION_1);
        counter.sample(ACTION_1);
        counter.sample(ACTION_2);
        counter.sample(ACTION_3);

        assertEquals(2, counter.getActionCount(ACTION_1));
        assertEquals(1, counter.getActionCount(ACTION_2));
        assertEquals(1, counter.getActionCount(ACTION_3));

        // State1 contains both ACTION_1 and ACTION_2!
        assertEquals(3, counter.getStateActionsCount("State1"));
        assertEquals(1, counter.getStateActionsCount("State2"));

        for (int i = 0; i < 532; i++) counter.sample(ACTION_1);
        for (int i = 0; i < 122; i++) counter.sample(ACTION_2);
        for (int i = 0; i < 48; i++)  counter.sample(ACTION_3);

        assertEquals(534, counter.getActionCount(ACTION_1));
        assertEquals(123, counter.getActionCount(ACTION_2));
        assertEquals(49,  counter.getActionCount(ACTION_3));

        // 534 + 123 = 657
        assertEquals(657, counter.getStateActionsCount("State1"));
        assertEquals(49,  counter.getStateActionsCount("State2"));
    }

    @Test
    public void testGetStateActionsCount() {
        assertEquals(0, counter.getStateActionsCount("State1"));
        assertEquals(0, counter.getStateActionsCount("State2"));

        counter.sample(ACTION_1);
        counter.sample(ACTION_2);
        counter.sample(ACTION_3);

        assertEquals(2, counter.getStateActionsCount("State1"));
        assertEquals(1, counter.getStateActionsCount("State2"));
        assertEquals(0, counter.getStateActionsCount("State3"));

        for (int i = 0; i < 10; i++) {
            counter.sample(ACTION_1);
            counter.sample(ACTION_2);
            counter.sample(ACTION_3);
        }

        assertEquals(22, counter.getStateActionsCount("State1"));
        assertEquals(11, counter.getStateActionsCount("State2"));
        assertEquals(0,  counter.getStateActionsCount("State3"));
    }

    @Test
    public void testGetActionCount() {
        assertEquals(0, counter.getActionCount(ACTION_1));
        assertEquals(0, counter.getActionCount(ACTION_2));
        assertEquals(0, counter.getActionCount(ACTION_3));

        counter.sample(ACTION_1);
        counter.sample(ACTION_2);
        counter.sample(ACTION_3);

        assertEquals(1, counter.getActionCount(ACTION_1));
        assertEquals(1, counter.getActionCount(ACTION_2));
        assertEquals(1, counter.getActionCount(ACTION_3));

        for (int i = 0; i < 10; i++) counter.sample(ACTION_1);

        assertEquals(11, counter.getActionCount(ACTION_1));
        assertEquals(1,  counter.getActionCount(ACTION_2));
        assertEquals(1,  counter.getActionCount(ACTION_3));
    }
}