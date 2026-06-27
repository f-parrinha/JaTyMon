package jatymon.monitor;

import com.palantir.javapoet.JavaFile;
import jatymon.exceptions.MonitorWriteFailedException;

import java.io.IOException;

/**
 * Class {@code Monitor Writer} allows the writing of java files given a monitor instance
 * @author Francisco Parrinh
 */
public class MonitorWriter {

    /**
     * Writes a monitor to a file, to a given output path
     * @param monitorData reference monitor
     * @param outputPath output path to write to
     */
    public static void write(final MonitorData monitorData, final String outputPath) {
        final JavaFile javaFile = monitorData.javaFile();
        try {
            if (outputPath != null && !outputPath.isEmpty()) {
                java.nio.file.Path path = java.nio.file.Paths.get(outputPath);
                javaFile.writeTo(path);
            } else {
                javaFile.writeTo(System.out);
            }
        } catch (IOException e) {
            throw new MonitorWriteFailedException(outputPath);
        }
    }
}
