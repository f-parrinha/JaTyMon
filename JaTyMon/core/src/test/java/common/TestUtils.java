package common;

import jatymon.common.FileUtils;
import jatymon.exceptions.FileDoesNotExistException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestUtils {

    /**
     * Returns the complete path of the Protocol file in the given relative (from src)
     * @param filePath Protocol file path
     * @return Protocol file complete path
     */
    public static Path getProtocolPath(String filePath) {
        try {
            return FileUtils.getPath(filePath, FileUtils.PROTOCOL_FILE_EXTENSION);
        } catch (FileDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the complete path of the JSON file in the given relative (from src)
     * @param filePath JSON file path
     * @return JSON file complete path
     */
    public static Path getJsonPath(String filePath) {
        try {
            return FileUtils.getPath(filePath, FileUtils.JSON_FILE_EXTENSION);
        } catch (FileDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the complete path of the Java file in the given relative (from src)
     * @param filePath Java file path
     * @return Java file complete path
     */
    public static Path getJavaPath(String filePath) {
        try {
            return FileUtils.getPath(filePath, FileUtils.JAVA_FILE_EXTENSION);
        } catch (FileDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Completely clears all files and folders in the given directory
     * @param directory directory to clear
     * @throws IOException no directory found
     */
    public static void cleanDirectory(final String directory) throws IOException {
        Path outputDir = Paths.get(directory);
        if (Files.exists(outputDir)) {
            try (Stream<Path> paths = Files.walk(outputDir)) {

                // Delete files before directories!
                paths.sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                System.err.println("Failed to delete: " + path + " - " + e.getMessage());
                            }
                        });
            }
        }
    }
}
