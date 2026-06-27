package monitor;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MonitorBasicTests extends MonitorTester {

    @Test
    public void testCircular() throws IOException {
        generateMonitors(false, true, Tests.Basic.CIRCULAR);
    }
    @Test
    public void testCircularWithGetter() throws IOException {
        generateMonitors(false, true, Tests.Basic.CIRCULAR_WITH_GETTER);
    }
    @Test
    public void testFile() throws IOException {
        generateMonitors(false, true, Tests.Basic.FILE);
    }
    @Test
    public void testFileInCollection() throws IOException {
        generateMonitors(false, true, Tests.Basic.FILE_IN_COLLECTION);
    }

    @Test
    public void testLinearity() throws IOException {
        generateMonitors(false, true, Tests.Basic.LINEARITY);
    }
}
