package shared.model;

/**
 * Exception for too many players - can't add any more players
 */
public class TooManyPlayersException extends Exception {
    public TooManyPlayersException(String message) {
        super(message);
    }
}
