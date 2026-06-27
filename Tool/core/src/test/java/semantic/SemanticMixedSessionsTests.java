package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SemanticMixedSessionsTests extends CompilationTester {
    @Test
    public void testDroneClient() throws IOException {
        success(Tests.MixedSessions.DRONE_CLIENT);
    }

    @Test
    public void testDroneServer() throws IOException {
        success(Tests.MixedSessions.DRONE_SERVER);
    }

    @Test
    public void testSender() throws IOException {
        success(Tests.MixedSessions.SENDER);
    }

    @Test
    public void testReceiver() throws IOException {
        success(Tests.MixedSessions.RECEIVER);
    }

    @Test
    public void testMixed() throws IOException {
        success(Tests.MixedSessions.MIXED);
    }

    @Test
    public void testMixed2() throws IOException {
        success(Tests.MixedSessions.MIXED_2);
    }
}
