import jatymon.babelprojects.abdquorum.protocols.client.ClientProtocol;
import jatymon.babelprojects.abdquorum.protocols.client.MonitoredClientProtocol;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorClientTests {
    private static MonitoredClientProtocol monitoredClient;

    @BeforeAll
    public static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        babel.registerProtocol(monitoredClient);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    /*
     * USE THIS METHOD IF CLIENT IS THE 'PROBABILISTIC CLIENT'
     */
    @Test
    public void testProbabilisticClientIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.sendReadMessage(true);
        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions (brute force state change bcs startClient throws exceptions and thus, the monitor cancels the transition)
        monitoredClient.startClient(ClientProtocol.Op.WRITE);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "Main");
        monitoredClient.sendReadMessage(true);
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
        monitoredClient.sendWriteMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "Main");   // opsNumb is 0 so a single write calls stopClient()
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);

        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal action
        monitoredClient.startClient(ClientProtocol.Op.WRITE);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));
    }

    /*
     * USE THIS METHOD IF CLIENT IS THE 'LINEAR CLIENT'
     */
    //@Test
    public void testLinearClientIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException {

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.collectReadStatistics();
        monitoredClient.sendReadMessage(true);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        monitoredClient.sendReadMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveRead");
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendWrite");
        monitoredClient.collectReadStatistics();
        monitoredClient.sendWriteMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveWrite");
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendRead");
        monitoredClient.collectWriteStatistics();

        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.sendWriteMessage(true);
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
        monitoredClient.collectReadStatistics();
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);

        // clientReadAck causes two exceptions (also calls send write). clientWriteAck causes one (calls send read)
        assertEquals(7, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        monitoredClient.sendWriteMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveWrite");
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendRead");
        monitoredClient.collectWriteStatistics();
        monitoredClient.sendReadMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveRead");
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendWrite");
        monitoredClient.collectReadStatistics();

        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.sendReadMessage(true);
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
        monitoredClient.collectReadStatistics();
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);

        assertEquals(8, logger.getLogTypeCount(IllegalActionLog.class));
    }

    @Test
    public void testInternalExceptionLog() throws HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        monitoredClient.init(MonitorTestsUtils.CLIENT_TEST_PROPS);

        // Internal exception!
        monitoredClient.uponClientReadAck(null, (short)1);
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }
}
