package common.compilation;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import common.TestUtils;
import common.Tester;
import jatymon.JaTyMonProcessor;
import jatymon.processing.ProcessorOptions;
import jatymon.diagnostics.DiagnosticsCollector;
import jatymon.diagnostics.AbstractDiagnostic;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CompilationTester implements Tester {
    public static final String COMPILATION_ERR_MSG = "%s [%s:%s] %s";

    /* --------- Success Test Methods ---------  */

    /**
     * Tests if a group of files were successfully compiled and processed by the typestate processor.
     * @param options map with options to be given to the compiler
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void success(final ProcessorOptions options,
                        final String... filesPath) throws IOException {
        final CompilationResult result = compile(options, filesPath);
        assertTrue(result.isSuccess());
        assertTrue(result.processor().isSuccess());
    }

    /**
     * Tests if a group of files were successfully compiled and processed by the typestate processor
     *  using {@code GENERATE = false} by default.
     * @param isSilent silence diagnostics
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void success(final boolean isSilent, final String... filesPath) throws IOException {
        success(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath);
    }

    /**
     * Tests if a group of files were successfully compiled and processed by the typestate processor
     *  using {@code SILENT = true} and {@code GENERATE = false} by default.
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void success(final String... filesPath) throws IOException {
        success(new ProcessorOptions().withSilent(true).withGenerate(false), filesPath);
    }

    /* --------- Failure Test Methods ---------  */

    /**
     * Tests if a group of files compiled with failure or the processing contains errors.
     * @param options map with options to be given to the compiler
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void failure(final ProcessorOptions options,
                        final String... filesPath) throws IOException {
        final CompilationResult result = compile(options, filesPath);
        assertFalse(result.isSuccess());
        assertFalse(result.processor().isSuccess());
    }

    /**
     * Tests if a group of files compiled with failure or if the typestate processing contains errors
     *  using {@code GENERATE = false} by default.
     * @param isSilent silence diagnostics
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void failure(final boolean isSilent, final String... filesPath) throws IOException {
        failure(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath);
    }

    /**
     * Tests if a group of files compiled with failure or if the typestate processing contains errors
     *  using {@code SILENT = true} and {@code GENERATE = false} by default.
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void failure(final String... filesPath) throws IOException {
        failure(new ProcessorOptions().withSilent(true).withGenerate(false), filesPath);
    }

    /* --------- Has Diagnostic Test Methods ---------  */

    /**
     * Tests if a group of files compiled and contains a list with the given diagnostics.
     *  Duplicate diagnostics must be included.
     *  using {@code SILENT = true} and {@code GENERATE = false} by default.
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void hasDiagnostic(final ProcessorOptions options,
                              final List<String>  filesPath,
                              final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        final String[] filesPathArray = new String[filesPath.size()];
        filesPath.toArray(filesPathArray);
        final CompilationResult result = compile(options, filesPathArray);
        final DiagnosticsCollector diagnosticsCollector = result.processor().getDiagnosticsCollector();
        for(var diagnostic : diagnostics) {
            assertTrue(diagnosticsCollector.containsDiagnostic(diagnostic));
        }
        assertEquals(diagnostics.size(), diagnosticsCollector.size());
    }

    /**
     * Tests if a group of files compiled and contains a list with the given diagnostics.
     *  Duplicate diagnostics must be included. Uses {@code GENERATE = false} by default.
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void hasDiagnostic(final boolean isSilent,
                              final List<String> filesPath,
                              final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        hasDiagnostic(new ProcessorOptions().withSilent(isSilent).withGenerate(false), filesPath, diagnostics);
    }

    /**
     * Tests if a group of files compiled and contains a list with the given diagnostics.
     *  Duplicate diagnostics must be included. Uses {@code SILENT = true} and {@code GENERATE = false} by default.
     * @param filesPath array with paths for the files
     * @throws IOException exception from trying to access the given files
     */
    public void hasDiagnostic(final List<String> filesPath,
                              final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException {
        hasDiagnostic(new ProcessorOptions().withSilent(true).withGenerate(false), filesPath, diagnostics);
    }


    /* --------- Other Methods ---------  */

    protected CompilationResult compile(final ProcessorOptions options,
                                        final String... filesPath) throws IOException {
        final JavaFileObject[] files = new JavaFileObject[filesPath.length];

        // Load java files
        for (int i = 0; i < filesPath.length; i++) {
            File javaFile = TestUtils.getJavaPath(filesPath[i]).toFile();
            files[i] = JavaFileObjects.forResource(javaFile.toURI().toURL());
        }

        final List<String> compilerOptions = new LinkedList<>();
        for (final Map.Entry<String, Object> entry : options.asMap().entrySet()) {
            compilerOptions.add(String.format("-A%s=%s", entry.getKey(), entry.getValue()));
        }
        final JaTyMonProcessor processor = new JaTyMonProcessor(JaTyMonProcessor.DeployMode.TEST);
        return new CompilationResult(Compiler.javac()
                .withProcessors(processor)
                .withOptions(compilerOptions)
                .compile(files), processor);
    }

}
