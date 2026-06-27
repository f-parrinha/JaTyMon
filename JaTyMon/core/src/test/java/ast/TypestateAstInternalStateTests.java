package ast;

import common.Tests;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TypestateAstInternalStateTests extends TypestateAstTester {

    @Test
    public void testABPDual() throws IOException {
        success(Tests.InternalState.ABP_DUAL);
    }

    @Test
    public void testABPSender() throws IOException {
        success(Tests.InternalState.ABP_SENDER);
    }

    @Test
    public void testABPReceiver() throws IOException {
        success(Tests.InternalState.ABP_RECEIVER);
    }

    @Test
    public void testBVPLeader() throws IOException {
        success(Tests.InternalState.BVP_LEADER);
    }

    @Test
    public void testBVPPeer() throws IOException {
        success(Tests.InternalState.BVP_PEER);
    }

    @Test
    public void testBadInternalState() throws IOException {
        success(Tests.InternalState.BAD_INTERNAL_STATE);
    }
}
