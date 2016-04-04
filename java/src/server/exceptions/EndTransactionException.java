package server.exceptions;

/**
 * Exception for an invalid player
 */
public class EndTransactionException extends Exception {
    public EndTransactionException(String message) {
        super(message);
    }
}
