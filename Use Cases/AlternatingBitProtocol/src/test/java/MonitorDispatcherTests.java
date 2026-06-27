import jatymon.babelprojects.abp.protocols.MonitoredDispatcherProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorDispatcherTests {
    private static MonitoredDispatcherProtocol monitoredDispatcher;

    @BeforeAll
    public  static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();

        monitoredDispatcher = new MonitoredDispatcherProtocol(logger, 0.95);
        babel.registerProtocol(monitoredDispatcher);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException, HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredDispatcher = new MonitoredDispatcherProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredDispatcher.uponAckMessage(MonitorTestsUtils.TEST_ACK_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));
        monitoredDispatcher.uponBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Creates internal exception: brute force set state
        monitoredDispatcher.init(new Properties());
        MonitorTestsUtils.changeCurrentStateVar(monitoredDispatcher, "CreateTCPChannel");

        // Creates internal exception: brute force set state
        monitoredDispatcher.createTcpChannel(MonitorTestsUtils.TEST_ADDRESS.toString(), String.valueOf(MonitorTestsUtils.TEST_PORT));
        MonitorTestsUtils.changeCurrentStateVar(monitoredDispatcher, "Connect");

        // Illegal actions
        monitoredDispatcher.uponInConnectionDown(MonitorTestsUtils.TEST_IN_CONNECTION_DOWN, (short)1);
        monitoredDispatcher.uponOutConnectionDown(MonitorTestsUtils.TEST_OUT_CONNECTION_DOWN, (short)1);
        assertEquals(4, logger.getLogTypeCount(IllegalActionLog.class));

        // Internally it will not increase the "conns" var, which is linked to the typestate, so it will not transition
        monitoredDispatcher.connectAll();
        monitoredDispatcher.uponInConnectionUp(MonitorTestsUtils.TEST_IN_CONNECTION_UP, (short)1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredDispatcher, "Main");
        assertEquals(4, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        for (int i = 0; i < 10; i++) {
            monitoredDispatcher.uponBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
            monitoredDispatcher.uponAckMessage(MonitorTestsUtils.TEST_ACK_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
        }

        // Illegal actions
        monitoredDispatcher.connectAll();
        assertEquals(5, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions. Need brute force transition though
        monitoredDispatcher.uponOutConnectionDown(MonitorTestsUtils.TEST_OUT_CONNECTION_DOWN, (short)1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredDispatcher, "Connect");

        // Illegal actions
        for (int i = 0; i < 50; i++) {
            monitoredDispatcher.uponAckMessage(MonitorTestsUtils.TEST_ACK_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
            monitoredDispatcher.uponBitMessage(MonitorTestsUtils.TEST_BIT_MESSAGE, MonitorTestsUtils.TEST_HOST, (short)1, 1);
        }
        assertEquals(105, logger.getLogTypeCount(IllegalActionLog.class));
    }


    @Test
    public void testInternalExceptionLog() throws HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredDispatcher = new MonitoredDispatcherProtocol(logger, 0.95);

        // Exceptions here!
        monitoredDispatcher.init(new Properties());
        monitoredDispatcher.createTcpChannel(MonitorTestsUtils.TEST_ADDRESS.toString(), String.valueOf(MonitorTestsUtils.TEST_PORT));
        monitoredDispatcher.uponInConnectionUp(null, (short)1);

        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertEquals(3, logger.getLogTypeCount(InternalExceptionLog.class));
    }
}
