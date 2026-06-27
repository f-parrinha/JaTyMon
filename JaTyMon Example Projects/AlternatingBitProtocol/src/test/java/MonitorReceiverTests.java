import jatymon.babelprojects.abp.protocols.DispatcherProtocol;
import jatymon.babelprojects.abp.protocols.MonitoredABPReceiverProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.ratio.RatioLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorReceiverTests {
    private static MonitoredABPReceiverProtocol monitoredABPReceiver;

    @BeforeAll
    public  static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();

        monitoredABPReceiver = new MonitoredABPReceiverProtocol(logger, 0.95);
        babel.registerProtocol(monitoredABPReceiver);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPReceiver = new MonitoredABPReceiverProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Received bit message calls sendAckMessage, which is currently illegal to!
        monitoredABPReceiver.receiveBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        assertEquals(3, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredABPReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        assertEquals(4, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredABPReceiver.receiveBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        monitoredABPReceiver.receiveBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        monitoredABPReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        assertEquals(5, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredABPReceiver.uponConnectionDown(MonitorTestsUtils.TEST_CONNECTION_DOWN_NOTIFICATION, (short)1);
    }

    @Test
    public void testInternalExceptionLog() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPReceiver = new MonitoredABPReceiverProtocol(logger, 0.95);
        monitoredABPReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);

        // Exception here!
        monitoredABPReceiver.receiveBitMessage(null, (short)1);

        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }

    @Test
    public void testRatioLogs95confidence() {
        testRatioLogs(0.95);
    }

    @Test
    public void testRatioLogs90confidence() {
        testRatioLogs(0.9);
    }

    @Test
    public void testRatioLogs85confidence() {
        testRatioLogs(0.85);
    }

    @Test
    public void testRatioLogs80confidence() {
        testRatioLogs(0.80);
    }

    @Test
    public void testRatioLogs75confidence() {
        testRatioLogs(0.75);
    }

    @Test
    public void testRatioLogs70confidence() {
        testRatioLogs(0.70);
    }

    private void testRatioLogs(double confidenceLevel) {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPReceiver = new MonitoredABPReceiverProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // These are all legal
        monitoredABPReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        monitoredABPReceiver.receiveBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        for (int i = 0; i < 5; i++) {
            monitoredABPReceiver.receiveBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
            // Then the sendAck is triggered inside receiveBitMessage
        }

        // Failure. This is the minimum amount to trigger an invalid ratio
        int minActionsCount = MonitorTestsUtils.getMinActionExecutionsForRatioLog(confidenceLevel, 0.5, 11, 6);
        for (int i = 0; i < minActionsCount; i++) {
            monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        }
        assertTrue(logger.containsLogType(RatioLog.class));
        assertEquals(1, logger.getLogTypeCount(RatioLog.class));

        // Continue to fail to test the auto ratio log removal later on
        for (int i = 0; i < 10; i++) {
            monitoredABPReceiver.sendAckMessage(MonitorTestsUtils.TEST_HOST, (byte)1);
        }
        assertEquals(11, logger.getLogTypeCount(RatioLog.class));

        // Minimum amount of normal actions to remove the log
        // TODO: calculate precisely the correct amount of times until it resets the ratio logs
    }
}
