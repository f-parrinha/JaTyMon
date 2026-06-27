import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.quorum.MonitoredABDReceiverProtocol;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.ratio.RatioLog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorReceiverTests {
    private static MonitoredABDReceiverProtocol monitoredABPReceiver;

    @BeforeAll
    public  static void init() throws ProtocolAlreadyExistsException, NoSuchFieldException, IllegalAccessException {
        MonitorTestsUtils.resetBabelInstance();

        final JaTyMonLogger logger = new JaTyMonLogger(false);
        final Babel babel = Babel.getInstance();
        final DispatcherProtocol dispatcherProto = new DispatcherProtocol();

        monitoredABPReceiver = new MonitoredABDReceiverProtocol(logger, 0.95);
        babel.registerProtocol(monitoredABPReceiver);
        babel.registerProtocol(dispatcherProto);
        babel.start();
    }

    @Test
    public void testIllegalActionLogs() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPReceiver = new MonitoredABDReceiverProtocol(logger, 0.95);
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));


        // TODO: finish test illegal actions logs
    }

    @Test
    public void testInternalExceptionLog() {
        final JaTyMonLogger logger = new JaTyMonLogger(false);
        monitoredABPReceiver = new MonitoredABDReceiverProtocol(logger, 0.95);

        // TODO: finish test internal exception logs
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
        monitoredABPReceiver = new MonitoredABDReceiverProtocol(logger, confidenceLevel);
        assertFalse(logger.containsLogType(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));

        // TODO: finish test ratio logs
    }
}
