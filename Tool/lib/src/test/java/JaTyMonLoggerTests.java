import jatymon.actions.ActionType;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.LoggerWriteFailedLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.intervals.BoundedInterval;
import jatymon.actions.ActionId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class JaTyMonLoggerTests {

    private JaTyMonLogger logger;

    // Fixed helpers
    private static final String TEST_TYPESTATE = "TestTypestate";
    private static final BoundedInterval TEST_INTERVAL = new BoundedInterval(0.3, 0.7);
    private static final ActionId TEST_RATIO_ACTION_1 = new ActionId("State1", "boot", ActionType.Input);
    private static final ActionId TEST_RATIO_ACTION_2 = new ActionId("State1", "close", ActionType.Input);
    private static final String TEST_EXCEPTION_MESSAGE = "Test exception message.";
    private static final String TEST_ACTION_NAME = "action";
    private static final String TEST_STATE_NAME = "State1";
    private static final double TEST_CONFIDENCE_LEVEL = 0.95;

    @BeforeAll
    static void setUpAll() {
        final File logDir = new File(JaTyMonLogger.LOG_DIR);
        if (logDir.exists()) {
            Arrays.stream(Objects.requireNonNull(logDir.listFiles((dir, name) -> name.contains(".log"))))
                    .forEach(File::delete);
        }
    }

    @BeforeEach
    void setUp() {
        logger = new JaTyMonLogger(false);
    }

    @Test
    public void testLog() {
        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));

        for (int i = 0; i < 5; i++) {
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
        }

        logger.log(new LoggerWriteFailedLog());

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(5, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(0, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(1, logger.getLogTypeCount(LoggerWriteFailedLog.class));
    }

    @Test
    public void testContainsLogType() {
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertFalse(logger.containsLogType(RatioLog.class));
        assertFalse(logger.containsLogType(InternalExceptionLog.class));
        assertFalse(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
        }

        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertFalse(logger.containsLogType(RatioLog.class));
        assertFalse(logger.containsLogType(InternalExceptionLog.class));
        assertFalse(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
        }

        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertTrue(logger.containsLogType(RatioLog.class));
        assertFalse(logger.containsLogType(InternalExceptionLog.class));
        assertFalse(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(new InternalExceptionLog(TEST_TYPESTATE, TEST_EXCEPTION_MESSAGE));
        }

        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertTrue(logger.containsLogType(RatioLog.class));
        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertFalse(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(new MonitorStartLog(TEST_TYPESTATE, TEST_CONFIDENCE_LEVEL));
        }

        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertTrue(logger.containsLogType(RatioLog.class));
        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertTrue(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(new LoggerWriteFailedLog());
        }

        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertTrue(logger.containsLogType(RatioLog.class));
        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertTrue(logger.containsLogType(MonitorStartLog.class));
        assertTrue(logger.containsLogType(LoggerWriteFailedLog.class));
        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(10, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(10, logger.getLogTypeCount(LoggerWriteFailedLog.class));
    }

    @Test
    public void testRemoveRatioLogs() {
        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.1, TEST_INTERVAL));
            logger.log(new MonitorStartLog(TEST_TYPESTATE, TEST_CONFIDENCE_LEVEL));
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        // Note that we are trying to remove using a different action id than the one logged
        logger.removeRatioLogs(TEST_RATIO_ACTION_2);

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        // Now we remove using the correct action id
        logger.removeRatioLogs(TEST_RATIO_ACTION_1);

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        for (int i = 0; i < 10; i++) {
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_2, 0.9, TEST_INTERVAL));
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(20, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        logger.removeRatioLogs(TEST_RATIO_ACTION_1);

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        logger.removeRatioLogs(TEST_RATIO_ACTION_2);

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));
    }

    @Test
    public void testClearLogs() {
        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
            logger.log(new InternalExceptionLog(TEST_TYPESTATE, TEST_EXCEPTION_MESSAGE));
            logger.log(new MonitorStartLog(TEST_TYPESTATE, TEST_CONFIDENCE_LEVEL));
            logger.log(new LoggerWriteFailedLog());
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(10, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(10, logger.getLogTypeCount(LoggerWriteFailedLog.class));
        assertTrue(logger.containsLogType(IllegalActionLog.class));
        assertTrue(logger.containsLogType(RatioLog.class));
        assertTrue(logger.containsLogType(InternalExceptionLog.class));
        assertTrue(logger.containsLogType(MonitorStartLog.class));
        assertTrue(logger.containsLogType(LoggerWriteFailedLog.class));

        logger.clearLogs();

        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(0, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));
        assertFalse(logger.containsLogType(IllegalActionLog.class));
        assertFalse(logger.containsLogType(RatioLog.class));
        assertFalse(logger.containsLogType(InternalExceptionLog.class));
        assertFalse(logger.containsLogType(MonitorStartLog.class));
        assertFalse(logger.containsLogType(LoggerWriteFailedLog.class));
    }

    @Test
    public void testGetLogTypeCount() {
        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
            logger.log(new InternalExceptionLog(TEST_TYPESTATE, TEST_EXCEPTION_MESSAGE));
            logger.log(new MonitorStartLog(TEST_TYPESTATE, TEST_CONFIDENCE_LEVEL));
            logger.log(new LoggerWriteFailedLog());
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(10, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(10, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        // Now, let's add a lot of logs
        for (int i = 0; i < 100; i++) {
            logger.log(new LoggerWriteFailedLog());
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(10, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(110, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        // Now, let's add a lot MORE logs
        for (int i = 0; i < 532; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
        }
        for (int i = 0; i < 122; i++) {
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
        }
        for (int i = 0; i < 48; i++) {
            logger.log(new InternalExceptionLog(TEST_TYPESTATE, TEST_EXCEPTION_MESSAGE));
        }

        assertEquals(542, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(132, logger.getLogTypeCount(RatioLog.class));
        assertEquals(58, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(110, logger.getLogTypeCount(LoggerWriteFailedLog.class));
    }

    @Test
    public void testWrite() {
        for (int i = 0; i < 10; i++) {
            logger.log(new IllegalActionLog(TEST_TYPESTATE, TEST_STATE_NAME, TEST_ACTION_NAME, ActionType.Input));
            logger.log(RatioLogFactory.buildLog(TEST_TYPESTATE, TEST_RATIO_ACTION_1, 0.8, TEST_INTERVAL));
            logger.log(new InternalExceptionLog(TEST_TYPESTATE, TEST_EXCEPTION_MESSAGE));
            logger.log(new MonitorStartLog(TEST_TYPESTATE, TEST_CONFIDENCE_LEVEL));
            logger.log(new LoggerWriteFailedLog());
        }

        assertEquals(10, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(10, logger.getLogTypeCount(RatioLog.class));
        assertEquals(10, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(10, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(10, logger.getLogTypeCount(LoggerWriteFailedLog.class));

        logger.write();

        assertEquals(0, logger.getLogTypeCount(IllegalActionLog.class));
        assertEquals(0, logger.getLogTypeCount(RatioLog.class));
        assertEquals(0, logger.getLogTypeCount(InternalExceptionLog.class));
        assertEquals(0, logger.getLogTypeCount(MonitorStartLog.class));
        assertEquals(0, logger.getLogTypeCount(LoggerWriteFailedLog.class));
    }
}
