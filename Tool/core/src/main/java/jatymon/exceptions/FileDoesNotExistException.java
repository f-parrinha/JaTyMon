package jatymon.exceptions;

/**
 * Custom exception for non-existing files (or wrong path for the wanted resource)
 * @author Francisco Parrinha
 */
public class FileDoesNotExistException extends Exception {
    public static final String MSG = "The file in the specified path '%s' does not exist";

    public FileDoesNotExistException(String filePath) {
        super(String.format(MSG, filePath));
    }
}
