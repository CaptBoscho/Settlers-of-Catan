package server.exceptions;

/**
 * Exception for error attempting to end a transaction
 */
public class EndTransactionException extends Exception {
    public EndTransactionException(String message) {
        super(message);
    }
}
