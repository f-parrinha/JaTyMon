package jatymon.diagnostics;

import jatymon.diagnostics.semantic.SemanticDiagnostic;
import jatymon.logging.JaTyMonMessager;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.*;

/**
 * Class {@code DiagnosticsCollector} collects and logs diagnostics, that should not be warned immediately (unlike exceptions).
 *  There are three kinds of diagnostics: {@code {Error, Warning, Note}}
 * @author Francisco Parrinha
 */
public class DiagnosticsCollector {
    public static final String HEADER_LOG = "\u001B[1m%s\u001B[0m Diagnostics";
    public static final String NO_DIAGNOSTICS_LOG = "(No diagnostics)";

    private final String name;
    private final List<ErrorDiagnostic> errorDiagnostics;
    private final List<WarningDiagnostic> warningDiagnostics;
    private final List<NoteDiagnostic> noteDiagnostics;
    private final Messager messager;
    private final boolean isSilent;

    /**
     * Creates a new {@code DiagnosticsCollector} with a given messager and its processor name
     * @param name name for the diagnostic session
     * @param messager messager to be used
     */
    public DiagnosticsCollector(final boolean isSilent, final String name, final Messager messager) {
        this.name = name;
        this.messager = messager;
        this.errorDiagnostics = new ArrayList<>();
        this.warningDiagnostics = new ArrayList<>();
        this.noteDiagnostics = new ArrayList<>();
        this.isSilent = isSilent;
    }

    /**
     * Creates a new {@code DiagnosticsCollector} with the {@code DiagnosticsMessager} messager
     */
    public DiagnosticsCollector(final boolean isSilent, final String name) {
        this(isSilent, name, new JaTyMonMessager());
    }

    /**
     * Are there no diagnostics?
     * @return true if no diagnostics where collected, false if not
     */
    public boolean isEmpty() {
        return errorDiagnostics.isEmpty() && warningDiagnostics.isEmpty() && noteDiagnostics.isEmpty();
    }

    /**
     * Logs immediately and DOES NOT collect the given diagnostic
     * @param diagnostic new diagnostic (any kind)
     */
    public void log(AbstractDiagnostic diagnostic) {
        if (isSilent) return;
        messager.printMessage(diagnostic.getKind(), diagnostic.getMessage());
    }

    /**
     * Logs all collected diagnostics
     */
    public void logCollected() {
        if (isSilent) return;

        System.out.println(/* Just an empty line */);
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(HEADER_LOG, name));
        boolean loggedAny = logCollectedNotes();
        loggedAny |= logCollectedWarnings();
        loggedAny |= logCollectedErrors();
        if (!loggedAny) {
            messager.printMessage(Diagnostic.Kind.NOTE, NO_DIAGNOSTICS_LOG);
        }
    }

    /**
     * Logs all collected notes
     */
    public boolean logCollectedNotes() {
        boolean containsLogs = !noteDiagnostics.isEmpty();
        if (isSilent) return containsLogs;

        noteDiagnostics.sort(getByTokenPositionComparator());
        for (final NoteDiagnostic diagnostic : noteDiagnostics) {
            messager.printMessage(diagnostic.getKind(), diagnostic.getMessage());
        }

        return containsLogs;
    }

    /**
     * Logs all collected warning
     */
    public boolean logCollectedWarnings() {
        boolean containsLogs = !warningDiagnostics.isEmpty();
        if (isSilent) return containsLogs;

        warningDiagnostics.sort(getByTokenPositionComparator());
        for (final WarningDiagnostic diagnostic : warningDiagnostics) {
            messager.printMessage(diagnostic.getKind(), diagnostic.getMessage());
        }

        return containsLogs;
    }

    /**
     * Logs all collected errors
     */
    public boolean logCollectedErrors() {
        boolean containsLogs = !errorDiagnostics.isEmpty();
        if (isSilent) return containsLogs;

        errorDiagnostics.sort(getByTokenPositionComparator());
        for (ErrorDiagnostic diagnostic : errorDiagnostics) {
            messager.printMessage(diagnostic.getKind(), diagnostic.getMessage());
        }
        return containsLogs;
    }

    /**
     * Collects a list of diagnostics, storing by their respective type.
     * @param diagnostics list of diagnostics
     */
    public void collectAll(List<AbstractDiagnostic> diagnostics) {
        for (var diagnostic : diagnostics) {
            if (diagnostic instanceof ErrorDiagnostic errorDiagnostic) {
                collect(errorDiagnostic);
            } else if (diagnostic instanceof WarningDiagnostic warningDiagnostic) {
                collect(warningDiagnostic);
            } else if (diagnostic instanceof NoteDiagnostic noteDiagnostic) {
                collect(noteDiagnostic);
            }
        }
    }

    /**
     * Collects error diagnostics
     * @param diagnostic new error diagnostic
     */
    public void collect(final ErrorDiagnostic diagnostic) {
        errorDiagnostics.add(diagnostic);
    }

    /**
     * Collects warning diagnostics
     * @param diagnostic new warning diagnostic
     */
    public void collect(final WarningDiagnostic diagnostic) {
        warningDiagnostics.add(diagnostic);
    }

    /**
     * Collects note diagnostics
     * @param diagnostic new note diagnostic
     */
    public void collect(final NoteDiagnostic diagnostic) {
        noteDiagnostics.add(diagnostic);
    }

    /**
     * Returns whether the manager contains any note diagnostic
     * @return true if it contains notes, false if note
     */
    public boolean containsNotes() {
        return !noteDiagnostics.isEmpty();
    }

    /**
     * Returns whether the manager contains any warning diagnostic
     * @return true if it contains warnings, false if note
     */
    public boolean containsWarnings() {
        return !warningDiagnostics.isEmpty();
    }

    /**
     * Returns whether the manager contains any error diagnostic
     * @return true if it contains errors, false if note
     */
    public boolean containsErrors() {
        return !errorDiagnostics.isEmpty();
    }

    /**
     * Returns the total number of diagnostics
     * @return total number of diagnostics
     */
    public int size() {
        return noteDiagnostics.size() + warningDiagnostics.size() + errorDiagnostics.size();
    }

    /**
     * Returns the number of note diagnostics
     * @return note diagnostics amount
     */
    public int notesSize() {
        return noteDiagnostics.size();
    }

    /**
     * Returns the number of warning diagnostics
     * @return warning diagnostics amount
     */
    public int warningsSize() {
        return warningDiagnostics.size();
    }

    /**
     * Returns the number of error diagnostics
     * @return error diagnostics amount
     */
    public int errorsSize() {
        return errorDiagnostics.size();
    }

    /**
     * Returns whether the manager has a given type of diagnostic
     * @param diagnosticType the type of the diagnostic
     * @return true if it contains, false if not
     * @param <T> a type of AbstractDiagnostic (any kind)
     */
    public <T extends AbstractDiagnostic> boolean containsDiagnostic(Class<T> diagnosticType) {
        List<? extends AbstractDiagnostic> diagnosticSet;
        if (ErrorDiagnostic.class.isAssignableFrom(diagnosticType)) {
            diagnosticSet = errorDiagnostics;
        } else if (WarningDiagnostic.class.isAssignableFrom(diagnosticType)) {
            diagnosticSet = warningDiagnostics;
        } else if (NoteDiagnostic.class.isAssignableFrom(diagnosticType)) {
            diagnosticSet = noteDiagnostics;
        } else {
            return false;
        }

        // Search for the exception in the correct set
        for (AbstractDiagnostic diagnostic : diagnosticSet) {
            if (diagnosticType.isInstance(diagnostic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the name of the processor being diagnosed
     * @return processor name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Error Diagnostics: %s\nWarning Diagnostics:%s\nNote Diagnostics: %s", errorDiagnostics, warningDiagnostics, noteDiagnostics);
    }

    private static <T extends AbstractDiagnostic> Comparator<T> getByTokenPositionComparator() {
        return (d1, d2) -> {
            if (d1 instanceof SemanticDiagnostic s1 && d2 instanceof SemanticDiagnostic s2) {
                return s1.getTokenPosition().compareTo(s2.getTokenPosition());
            }
            return 0;
        };
    }
}
