package jatymon.exceptions;

/**
 * Custom exception for enums that do not exist in the source code
 * @author Francisco Parrinha
 */
public class UnknownEnumException extends Exception {
    public static final String MESSAGE = "No enum with name '%s' could be resolved";
    public UnknownEnumException(String enumName) {
        super(String.format(MESSAGE, enumName));
    }
}
