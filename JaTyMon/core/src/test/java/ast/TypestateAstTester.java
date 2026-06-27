package ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.JsonDiffChecker;
import common.TestUtils;
import common.Tester;
import jatymon.JaTyMonProcessor;
import jatymon.diagnostics.DiagnosticsCollector;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.TypestateAst;
import jatymon.typestate.ast.TypestateAstFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypestateAstTester implements Tester {
    public static final String NULL_ENTRY = "<null>";
    public static final String EXPECTED_ENTRY = "actual:";
    public static final String ACTUAL_ENTRY = "expected:";

    public void success(final boolean isSilent, final String filePath) throws IOException {
        final Path protocolPath = TestUtils.getProtocolPath(filePath);
        final DiagnosticsCollector diagnosticsCollector = new DiagnosticsCollector(isSilent, JaTyMonProcessor.PROCESSOR_NAME);
        final TypestateAst ast = TypestateAstFactory.build(protocolPath);

        // Make sure there are no parsing errors
        diagnosticsCollector.collectAll(ast.diagnostics());
        if (!isSilent) {
            diagnosticsCollector.logCollected();
        }
        assertFalse(diagnosticsCollector.containsErrors());

        // Compare with correct AST
        final ObjectMapper mapper = new ObjectMapper();
        final File jsonFile = TestUtils.getJsonPath(filePath).toFile();
        final JsonNode expected = mapper.readTree(jsonFile);
        final JsonNode actual = mapper.valueToTree(ast.root().toJson());
        final List<JsonDiffChecker.DiffEntry> diffs = new ArrayList<>();

        JsonDiffChecker.diff(expected, actual, "", diffs);
        if (!diffs.isEmpty()) {
            fail(printAstDiffs(diffs));
        }
    }

    @Override
    public void success(final boolean isSilent, final String... filesPath) throws IOException {
        for (final var filePath : filesPath) {
            success(isSilent, filePath);
        }
    }

    @Override
    public void success(final String... filesPath) throws IOException {
        success(true, filesPath);
    }

    public void failure(final boolean isSilent, final String filePath) throws IOException {
        final Path protocolPath = TestUtils.getProtocolPath(filePath);
        final DiagnosticsCollector diagnosticsCollector = new DiagnosticsCollector(isSilent, JaTyMonProcessor.PROCESSOR_NAME);
        final TypestateAst ast = TypestateAstFactory.build(protocolPath);

        diagnosticsCollector.collectAll(ast.diagnostics());
        if (!isSilent) {
            diagnosticsCollector.logCollected();
        }

        assertTrue(diagnosticsCollector.containsErrors());
    }

    @Override
    public void failure(final boolean isSilent, final String... filesPath) throws IOException {
        for (var filePath : filesPath) {
            failure(isSilent, filePath);
        }
    }

    @Override
    public void failure(final String... filesPath) throws IOException {
        failure(true, filesPath);
    }

    public void hasDiagnostic(final boolean isSilent,
                              final String filePath,
                              final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        final Path protocolPath = TestUtils.getProtocolPath(filePath);
        final DiagnosticsCollector diagnosticsCollector = new DiagnosticsCollector(isSilent, JaTyMonProcessor.PROCESSOR_NAME);
        final TypestateAst ast = TypestateAstFactory.build(protocolPath);

        // Verify if it contains the required diagnostics
        diagnosticsCollector.collectAll(ast.diagnostics());
        diagnosticsCollector.logCollected();

        for(var diagnostic : diagnostics) {
            assertTrue(diagnosticsCollector.containsDiagnostic(diagnostic));
        }
    }

    public void hasDiagnostic(final String filePath, final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        hasDiagnostic(true, filePath, diagnostics);
    }

    @Override
    public void hasDiagnostic(final List<String> filesPath, final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        hasDiagnostic(true, filesPath, diagnostics);
    }

    @Override
    public void hasDiagnostic(final boolean isSilent, final List<String> filesPath, final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        for (var filePath : filesPath) {
            hasDiagnostic(isSilent, filePath, diagnostics);
        }
    }

    private static String printAstDiffs(final List<JsonDiffChecker.DiffEntry> diffs) {
        final StringBuilder sb = new StringBuilder();
        for (var d : diffs) {
            final JsonNode expected = d.expected();
            final JsonNode actual = d.actual();
            sb.append(d.path()).append("\n");
            sb.append(EXPECTED_ENTRY).append("\t").append(expected == null ? NULL_ENTRY : expected.toPrettyString()).append("\n");
            sb.append(ACTUAL_ENTRY).append("\t").append(actual == null ? NULL_ENTRY : actual.toPrettyString()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }
}
