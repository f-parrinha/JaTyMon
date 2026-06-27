import jatymon.babelprojects.multipaxos.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.smr.MonitoredMultiPaxosProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MonitorMultiPaxosTests {
    private static MonitoredMultiPaxosProtocol monitoredMultiPaxos;

    @BeforeAll
    public static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();
        monitoredMultiPaxos = new MonitoredMultiPaxosProtocol(logger, 0.95);
        babel.registerProtocol(monitoredMultiPaxos);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() throws NoSuchFieldException, IllegalAccessException, HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredMultiPaxos = new MonitoredMultiPaxosProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal actions
        monitoredMultiPaxos.uponAcceptMessage(MonitorTestsUtils.TEST_ACCEPT_MESSAGE_NOTIFICATION, (short) 1);
        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Legal actions (brute force state change bcs init throws exceptions and thus, the monitor cancels the transition)
        monitoredMultiPaxos.init(MonitorTestsUtils.TEST_PROPS);
        MonitorTestsUtils.changeCurrentStateVar(monitoredMultiPaxos, "Connect");
        monitoredMultiPaxos.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredMultiPaxos, "NonLeader");
        monitoredMultiPaxos.uponClientWriteMessage(MonitorTestsUtils.TEST_CLIENT_WRITE_MESSAGE_NOTIFICATION, (short) 1);
        monitoredMultiPaxos.uponPrepareOkMessage(MonitorTestsUtils.TEST_PREPARE_OK_MESSAGE_NOTIFICATION, (short) 1);
        monitoredMultiPaxos.uponAcceptOkMessage(MonitorTestsUtils.TEST_ACCEPT_OK_MESSAGE_NOTIFICATION, (short) 1);
        MonitorTestsUtils.changeCurrentStateVar(monitoredMultiPaxos, "Leader");
        monitoredMultiPaxos.uponClientReadMessage(MonitorTestsUtils.TEST_CLIENT_READ_MESSAGE_NOTIFICATION, (short) 1);

        assertEquals(1, logger.getLogTypeCount(IllegalActionLog.class));

        // Illegal action
        monitoredMultiPaxos.uponConnectionUp(MonitorTestsUtils.TEST_CONNECTION_UP_NOTIFICATION, (short) 1);
        assertEquals(2, logger.getLogTypeCount(IllegalActionLog.class));
    }

    @Test
    public void testInternalExceptionLog() throws HandlerRegistrationException, IOException {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredMultiPaxos = new MonitoredMultiPaxosProtocol(logger, 0.95);

        // Internal exception!
        monitoredMultiPaxos.uponAcceptMessage(null, (short)1);
        assertEquals(1, logger.getLogTypeCount(InternalExceptionLog.class));
    }
}
