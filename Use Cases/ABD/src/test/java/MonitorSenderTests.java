import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.quorum.MonitoredABDSenderProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.ratio.RatioLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorSenderTests {
    private static MonitoredABDSenderProtocol monitoredABPSender;

    @BeforeAll
    public static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();
        monitoredABPSender = new MonitoredABDSenderProtocol(logger, 0.95);
        babel.registerProtocol(monitoredABPSender);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPSender = new MonitoredABDSenderProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));

        // TODO: finish test illegal actions logs
    }

    @Test
    public void testRatioLogs95confidence() {
        final double confidenceLevel = 0.95;
        final double trueRatio = 0.5;       // Same as in the protocol!
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPSender = new MonitoredABDSenderProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // TODO: finish test ratio logs
    }

    @Test
    public void testInternalExceptionLog() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPSender = new MonitoredABDSenderProtocol(logger, 0.95);

        // TODO: finish test internal exceptions logs
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
        testRatioLogs(0.8);
    }

    @Test
    public void testRatioLogs75confidence() {
        testRatioLogs(0.75);
    }

    @Test
    public void testRatioLogs70confidence() {
        testRatioLogs(0.7);
    }

    private void testRatioLogs(double confidenceLevel) {
        final double trueRatio = 0.5;       // Same as in the protocol!
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPSender = new MonitoredABDSenderProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // TODO: finish test ratio logs
    }
}
