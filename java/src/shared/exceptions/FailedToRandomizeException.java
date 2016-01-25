package shared.exceptions;

/**
 * Exception for failure of randomizing
 */
public class FailedToRandomizeException extends Exception {
    public FailedToRandomizeException(String message) {
        super(message);
    }
}