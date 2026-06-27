package jatymon.logging;

import jatymon.logging.logs.AbstractLog;
import jatymon.logging.logs.internal.LoggerWriteFailedLog;
import jatymon.logging.logs.ratio.RatioLog;
import jatymon.actions.ActionId;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * TODO: customize write timeout
 * Custom Logger for the JaTyMon tool. Stores in memory each log and writes them to a file after some time
 * @author Francisco Parrinha
 */
public class JaTyMonLogger {
    public static final String LOG_DIR = "JaTyMon-Logs";
    public static final String LOG_FILE ="JaTyMon-report.log-%s";

    // Clear every 24 hours
    public static final long DEFAULT_CLEAR_TIMEOUT = 24 * 60 * 1000;

    private final long clearTimeout;
    private final boolean printMessages;
    private final JaTyMonMessager messager;
    private final Set<AbstractLog> logs;
    private final HashMap<Class<?>, Integer> logTypeCounters;

    // Requires a specific map because ratio logs must be discarded when the program starts behaving correctly
    private final Map<ActionId, Set<RatioLog>> ratioLogs;

    private long lastClearedAt;

    public JaTyMonLogger(boolean printMessages) {
        this.messager = new JaTyMonMessager();
        this.logs = new LinkedHashSet<>();
        this.ratioLogs = new HashMap<>();
        this.logTypeCounters = new HashMap<>();
        this.printMessages = printMessages;
        this.lastClearedAt = System.currentTimeMillis();
        this.clearTimeout = DEFAULT_CLEAR_TIMEOUT;
    }

    public JaTyMonLogger() {
        this(true);
    }

    /**
     * Stores and prints a new log. Stored logs are written periodically to a file
     * @param log new log to store and to print
     */
    public void log(final AbstractLog log) {
        if (System.currentTimeMillis() - lastClearedAt >= clearTimeout) {
            clearLogs();
            write();
            lastClearedAt = System.currentTimeMillis();
        }

        storeLog(log);
        final Class<?> key = log instanceof RatioLog ? RatioLog.class : log.getClass();
        logTypeCounters.compute(key, (k, counter) -> counter == null ? 1 : counter + 1);
        if (printMessages) {
            messager.printMessage(log.getKind(), log.getMessage());
        }
    }

    /**
     * Returns whether the logger contains the given log type or not
     * @param logType class of the log
     * @return if the logger contains any log that is instance of the given class
     */
    public boolean containsLogType(final Class<? extends AbstractLog> logType) {
        return getLogTypeCount(logType) > 0;
    }

    /**
     * Returns the number of stored logs of the given type
     * @param logType class of the log
     * @return number of logs that are instances of the given class
     */
    public int getLogTypeCount(final Class<? extends AbstractLog> logType) {
        return logTypeCounters.getOrDefault(logType, 0);
    }

    /**
     * Removes all ratio logs stored in memory. It does not remove their corresponding text written in the console
     * @param actionId pair of state and method names identifying the action containing ratio logs
     */
    public void removeRatioLogs(final ActionId actionId) {
        final Set<RatioLog> removed = ratioLogs.remove(actionId);
        if (removed != null) {
            logTypeCounters.computeIfPresent(RatioLog.class, (k, current) -> current - removed.size());
        }
    }

    /**
     * Removes all logs stored in memory
     */
    public void clearLogs() {
        ratioLogs.clear();
        logs.clear();
        logTypeCounters.clear();
    }

    /**
     * Writes all logs to a file
     */
    public void write() {
        final String path = "%s/%s".formatted(LOG_DIR, LOG_FILE.formatted(LocalDateTime.now()));
        new File(LOG_DIR).mkdirs();
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            for (final AbstractLog log : logs) {
                writer.write(log.getMessage());
                writer.newLine();
            }
            for (final Set<RatioLog> ratioLogSet : ratioLogs.values()) {
                for (final RatioLog log : ratioLogSet) {
                    writer.write(log.getMessage());
                    writer.newLine();
                }
            }
            clearLogs();
        } catch (final IOException e) {
            log(new LoggerWriteFailedLog());
        }
    }

    private void storeLog(final AbstractLog log) {
        if (log instanceof RatioLog ratioLog) {
            ratioLogs.computeIfAbsent(ratioLog.getActionId(), c -> new LinkedHashSet<>()).add(ratioLog);
        } else {
            logs.add(log);
        }
    }
}
