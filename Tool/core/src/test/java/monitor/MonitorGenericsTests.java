package monitor;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MonitorGenericsTests extends MonitorTester {

    @Test
    public void testValidGeneric1() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_1);
    }

    @Test
    public void testValidGeneric2() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_2);
    }

    @Test
    public void testValidGeneric3() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_3);
    }

    @Test
    public void testValidGeneric4() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_4);
    }

    @Test
    public void testValidGeneric5() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_5);
    }

    @Test
    public void testValidGeneric6() throws IOException {
        generateMonitors(false, true, Tests.Generics.VALID_GENERIC_6);
    }
}
