package server.exceptions;

/**
 * Exception for an invalid player
 */
public class DiscardCardsException extends Exception {
    public DiscardCardsException(String message) {
        super(message);
    }
}
