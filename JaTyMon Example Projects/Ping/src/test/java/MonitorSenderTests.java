import jatymon.babelprojects.ping.protocols.DispatcherProtocol;
import jatymon.babelprojects.ping.protocols.MonitoredPingSenderProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.ratio.RatioLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorSenderTests {
    private static MonitoredPingSenderProtocol monitoredSender;

    @BeforeAll
    public static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();
        monitoredSender = new MonitoredPingSenderProtocol(logger, 0.95);
        babel.registerProtocol(monitoredSender);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredSender = new MonitoredPingSenderProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredSender.broadcastEchoRequest();
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal action
        monitoredSender.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredSender, "State1");

        // Illegal action
        monitoredSender.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        assertEquals(3, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        monitoredSender.broadcastEchoRequest();
        monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        monitoredSender.broadcastEchoRequest();
        monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        monitoredSender.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);

        // Illegal action
        assertEquals(4, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredSender.uponConnectionDown(MonitorTestsUtils.TEST_CONNECTION_DOWN_NOTIFICATION, (short)1);
    }

    @Test
    public void testInternalExceptionLog() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredSender = new MonitoredPingSenderProtocol(logger, 0.95);
        monitoredSender.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        monitoredSender.broadcastEchoRequest();

        // Exception here!
        monitoredSender.uponReceiveEchoReply(null, (short)1);
        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }

    @Test
    public void testRatioLogs95confidence() throws NoSuchFieldException, IllegalAccessException {
        final double confidenceLevel = 0.95;
        final double trueRatio = 0.5;       // Same as in the protocol!
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredSender = new MonitoredPingSenderProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // These are all legal
        MonitorTestsUtils.changeCurrentStateVar(monitoredSender, "State1");
        for (int i = 0; i < 5; i++) {
            monitoredSender.broadcastEchoRequest();
            monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        }

        // Failure. This is the minimum amount to trigger an invalid ratio
        int minActionsCount =  MonitorTestsUtils.getMinActionExecutionsForRatioLog(confidenceLevel, trueRatio, 10, 5);
        for (int i = 0; i < minActionsCount; i++) {
            monitoredSender.broadcastEchoRequest();
        }
        assertTrue(logger.containsLogType(RatioLog.class));
        assertEquals(1, logger.getLogTypeCount(RatioLog.class));

        // Continue to fail to test the auto ratio log removal later on
        for (int i = 0; i < 10; i++) {
            monitoredSender.broadcastEchoRequest();
        }
        assertEquals(11, logger.getLogTypeCount(RatioLog.class));

        // Minimum amount of normal actions to remove the log
        // TODO: calculate precisely the correct amount of times until it resets the ratio logs
        for (int i = 0; i < 39; i++) {
            monitoredSender.broadcastEchoRequest();
            monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        }
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
    }

    @Test
    public void testRatioLogs90confidence() throws NoSuchFieldException, IllegalAccessException {
        testRatioLogs(0.9);
    }

    @Test
    public void testRatioLogs85confidence() throws NoSuchFieldException, IllegalAccessException {
        testRatioLogs(0.85);
    }

    @Test
    public void testRatioLogs80confidence() throws NoSuchFieldException, IllegalAccessException {
        testRatioLogs(0.8);
    }

    @Test
    public void testRatioLogs75confidence() throws NoSuchFieldException, IllegalAccessException {
        testRatioLogs(0.75);
    }

    @Test
    public void testRatioLogs70confidence() throws NoSuchFieldException, IllegalAccessException {
        testRatioLogs(0.7);
    }

    private void testRatioLogs(double confidenceLevel) throws NoSuchFieldException, IllegalAccessException {
        final double trueRatio = 0.5;       // Same as in the protocol!
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredSender = new MonitoredPingSenderProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // These are all legal
        MonitorTestsUtils.changeCurrentStateVar(monitoredSender, "State1");
        for (int i = 0; i < 50; i++) {
            monitoredSender.broadcastEchoRequest();
            monitoredSender.uponReceiveEchoReply(MonitorTestsUtils.TEST_ACK_MESSAGE_NOTIFICATION, (short)1);
        }

        // Failure. This is the minimum amount to trigger an invalid ratio
        int minActionsCount = MonitorTestsUtils.getMinActionExecutionsForRatioLog(confidenceLevel, trueRatio, 100, 50);
        for (int i = 0; i < minActionsCount; i++) {
            monitoredSender.broadcastEchoRequest();
        }
        assertTrue(logger.containsLogType(RatioLog.class));
        assertEquals(1, logger.getLogTypeCount(RatioLog.class));

        // Continue to fail to test the auto ratio log removal later on
        for (int i = 0; i < 10; i++) {
            monitoredSender.broadcastEchoRequest();
        }
        assertEquals(11, logger.getLogTypeCount(RatioLog.class));


        // TODO: Test ratio removal
    }
}
