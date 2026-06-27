package monitor;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MonitorInternalStateTests extends MonitorTester {

    @Test
    public void testABPDual() throws IOException {
        generateMonitors(true, false, true, Tests.InternalState.ABP_DUAL);
    }

    @Test
    public void testABPReceiver() throws IOException {
        generateMonitors(true, false, true, Tests.InternalState.ABP_RECEIVER);
    }

    @Test
    public void testABPSender() throws IOException {
        generateMonitors(true, false, true, Tests.InternalState.ABP_SENDER);
    }


    @Test
    public void testBVPLeader() throws IOException {
        generateMonitors(true, false, true, Tests.InternalState.BVP_LEADER);
    }


    @Test
    public void testBVPPeer() throws IOException {
        generateMonitors(true, false, true, Tests.InternalState.BVP_PEER);
    }
}
