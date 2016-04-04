package server.exceptions;

/**
 * Exception for an invalid player
 */
public class StartTransactionException extends Exception {
    public StartTransactionException(String message) {
        super(message);
    }
}
