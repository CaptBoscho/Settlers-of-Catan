package server.exceptions;

/**
 * Exception for error attempting to start a new transaction
 */
public class StartTransactionException extends Exception {
    public StartTransactionException(String message) {
        super(message);
    }
}
