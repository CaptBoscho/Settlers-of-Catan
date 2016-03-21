package server.exceptions;

/**
 * Exception for an invalid player
 */
public class AcceptTradeException extends Exception {
    public AcceptTradeException(String message) {
        super(message);
    }
}
