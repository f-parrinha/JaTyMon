package monitor;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MonitorKeyTests extends MonitorTester {

    @Test
    public void testKeyBasic1() throws IOException {
        generateMonitors(true, false, true, Tests.Key.KEY_BASIC_1);
    }
}
