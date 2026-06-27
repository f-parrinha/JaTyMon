import jatymon.babelprojects.ping.protocols.DispatcherProtocol;
import jatymon.babelprojects.ping.protocols.MonitoredPingReceiverProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorReceiverTests {
    private static MonitoredPingReceiverProtocol monitoredReceiver;

    @BeforeAll
    public  static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();

        monitoredReceiver = new MonitoredPingReceiverProtocol(logger, 0.95);
        babel.registerProtocol(monitoredReceiver);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredReceiver = new MonitoredPingReceiverProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Received bit message calls sendAckMessage, which is currently illegal to!
        monitoredReceiver.uponReceivedEchoRequest(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredReceiver.sendEchoReply(MonitorTestsUtils.TEST_HOST);
        assertEquals(3, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);

        MonitorTestsUtils.changeCurrentStateVar(monitoredReceiver, "State1");
        monitoredReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        assertEquals(4, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        monitoredReceiver.uponReceivedEchoRequest(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        monitoredReceiver.sendEchoReply(MonitorTestsUtils.TEST_HOST);
        monitoredReceiver.uponReceivedEchoRequest(MonitorTestsUtils.TEST_BIT_MESSAGE_NOTIFICATION, (short)1);
        monitoredReceiver.sendEchoReply(MonitorTestsUtils.TEST_HOST);

        // Illegal action
        monitoredReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);
        assertEquals(5, logger.getLogTypeCount(IllegalActionLog.class));

        monitoredReceiver.uponConnectionDown(MonitorTestsUtils.TEST_CONNECTION_DOWN_NOTIFICATION, (short)1);
    }

    @Test
    public void testInternalExceptionLog() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredReceiver = new MonitoredPingReceiverProtocol(logger, 0.95);
        monitoredReceiver.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short)1);

        // Exception here!
        monitoredReceiver.uponReceivedEchoRequest(null, (short)1);

        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }
}
