import jatymon.babelprojects.multipaxos.session.Operation;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.client.MonitoredClientProtocol;
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

    @Test
    public void testProbabilisticClientIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException {
        // USE THIS METHOD CLIENT IS THE 'PROBABILISTIC CLIENT'

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.sendReadMessage(true);
        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions (brute force state change bcs startClient throws exceptions and thus, the monitor cancels the transition)
        monitoredClient.startClient(Operation.Type.WRITE);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "Main");
        monitoredClient.sendReadMessage(true);
        monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
        monitoredClient.sendWriteMessage(true);
        MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "Main");   // opsNumb is 0 so a single write calls stopClient()
        monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);

        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal action
        monitoredClient.startClient(Operation.Type.WRITE);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));
    }

    @Test
    public void testLinearClientIllegalActionLogs() throws HandlerRegistrationException, IOException, NoSuchFieldException, IllegalAccessException {
        // USE THIS METHOD CLIENT IS THE 'LINEAR CLIENT'
        /*
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredClient.collectReadStatistics();
        monitoredClient.sendReadMessage();
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions
        ClientProtocol.Op firstOp = monitoredClient.getFirstOp();
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

        if (firstOp == ClientProtocol.Op.READ) {

            // Legal actions
            monitoredClient.sendReadMessage();
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveRead");
            monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendWrite");
            monitoredClient.collectReadStatistics();
            monitoredClient.sendWriteMessage();
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveWrite");
            monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendRead");
            monitoredClient.collectWriteStatistics();

            assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

            // Illegal actions
            monitoredClient.sendWriteMessage();
            monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
            monitoredClient.collectReadStatistics();
            monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);

            // clientReadAck causes two exceptions (also calls send write). clientWriteAck causes one (calls send read)
            assertEquals(7, logger.getLogTypeCount(IllegalActionLog.class));
        } else if (firstOp == ClientProtocol.Op.WRITE) {

            // Legal actions
            monitoredClient.sendWriteMessage();
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveWrite");
            monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendRead");
            monitoredClient.collectWriteStatistics();
            monitoredClient.sendReadMessage();
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "ReceiveRead");
            monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);
            MonitorTestsUtils.changeCurrentStateVar(monitoredClient, "SendWrite");
            monitoredClient.collectReadStatistics();

            assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));

            // Illegal actions
            monitoredClient.sendReadMessage();
            monitoredClient.uponClientWriteAck(MonitorTestsUtils.TEST_CLIENT_WRITE_ACK_NOTIFICATION, (short) 1);
            monitoredClient.collectReadStatistics();
            monitoredClient.uponClientReadAck(MonitorTestsUtils.TEST_CLIENT_READ_ACK_NOTIFICATION, (short) 1);

            assertEquals(8, logger.getLogTypeCount(IllegalActionLog.class));
        }
        */
    }

    @Test
    public void testInternalExceptionLog() throws HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredClient = new MonitoredClientProtocol(logger, 0.95);
        monitoredClient.init(MonitorTestsUtils.TEST_PROPS);

        // Internal exception!
        monitoredClient.uponClientReadAck(null, (short)1);
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }
}
