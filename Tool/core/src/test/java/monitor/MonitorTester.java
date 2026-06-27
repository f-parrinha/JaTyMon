package monitor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import common.compilation.CompilationResult;
import common.compilation.CompilationTester;
import common.TestArgs;
import common.TestUtils;
import jatymon.processing.ProcessorOptions;
import jatymon.logging.JaTyMonMessager;
import jatymon.monitor.MonitorData;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorTester extends CompilationTester {
    public static final String DEFAULT_OUTPUT_PATH =  Paths.get("").toAbsolutePath().resolve("generated-monitors").toString();
    public static final String MONITOR_COMPILATION_ERR_MSG = "Monitor compilation failed: %s";

    public void generateMonitors(boolean compile, boolean autoClean, boolean isSilent, String... filesPath) throws IOException {
        final String outputPath = System.getProperty(TestArgs.OUTPUT_PATH, DEFAULT_OUTPUT_PATH);
        final ProcessorOptions options = new ProcessorOptions()
                .withSilent(isSilent)
                .withGenerate(true)
                .withOutput(outputPath);

        // Check processing errors
        final CompilationResult result = compile(options, filesPath);
        assertTrue(result.isSuccess());
        assertTrue(result.processor().isSuccess());

        if (compile) {
            // Copy original class because the monitors extend these classes and need access to them
            for (final String filePath : filesPath) {
                final Path source = TestUtils.getJavaPath(filePath);
                final Path relative = source.getParent().getFileName().resolve(source.getFileName());
                final Path destination = Paths.get(outputPath).resolve(relative);
                Files.createDirectories(destination.getParent());
                //Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            // Compile the monitors
            for (final MonitorData monitorData : result.processor().getProcessedMonitors().values()) {
                final Compilation compilation = compileMonitor(monitorData);
                assertEquals(Compilation.Status.SUCCESS, compilation.status(), String.format(MONITOR_COMPILATION_ERR_MSG, monitorData.qualifiedName()));
            }
        }

        // Auto clean generated content
        if (autoClean) {
            TestUtils.cleanDirectory(outputPath);
        }
    }

    public void generateMonitors(boolean autoClean, boolean isSilent, String... filesPath) throws IOException {
        generateMonitors(false, autoClean, isSilent, filesPath);
    }

    public void generateMonitors(boolean isSilent, String... filesPath) throws IOException {
        generateMonitors(true, isSilent, filesPath);
    }

    public void generateMonitors(String... filesPath) throws IOException {
        generateMonitors(true,true, filesPath);
    }


    /* ---------------------- AUX METHODS ---------------------- */


    private Compilation compileMonitor(final MonitorData monitorData) {
        final JavaFileObject source = JavaFileObjects.forSourceString(monitorData.qualifiedName(), monitorData.javaFile().toString());
        final Compilation compilation = Compiler.javac().compile(source);

        // Display errors
        final JaTyMonMessager messager = new JaTyMonMessager();
        compilation.errors().forEach(d -> messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(COMPILATION_ERR_MSG, d.getSource().getName(), d.getLineNumber(), d.getColumnNumber(), d.getMessage(null))));

        return compilation;
    }
}
