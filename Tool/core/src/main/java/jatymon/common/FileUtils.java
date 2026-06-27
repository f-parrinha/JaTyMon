package jatymon.common;

import jatymon.exceptions.FileDoesNotExistException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class {@code FileUtils} contains auxiliary methods for handling files.
 * @author Francisco Parrinha
 */
public class FileUtils {
    public static final String PROTOCOL_FILE_EXTENSION = ".protocol";
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String JAVA_FILE_EXTENSION = ".java";

    public static File getFile(final Path filePath) {
        return filePath.toFile();
    }

    public static File getFile(final String filePath) throws FileDoesNotExistException {
        return getPath(filePath).toFile();
    }

    public static File getFile(final String filePath, final String fileExtension) throws FileDoesNotExistException {
        return getPath(filePath, fileExtension).toFile();
    }

    public static Path getPath(final String filePath) throws FileDoesNotExistException {
        return getPath(filePath, "");
    }

    public static Path getPath(String filePath, final String fileExtension) throws FileDoesNotExistException {
        if (!filePath.endsWith(fileExtension)) {
            filePath = filePath.concat(fileExtension);
        }

        // Load URL
        final URL resource = FileUtils.class.getClassLoader().getResource(filePath);
        if (resource == null) {
            throw new FileDoesNotExistException(filePath);
        }

        // Load final path (useful bcs URL might be 'escaped')
        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
