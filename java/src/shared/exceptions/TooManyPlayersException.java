package shared.exceptions;

/**
 * Exception for too many players - can't add any more players
 */
public class TooManyPlayersException extends Exception {
    public TooManyPlayersException(String message) {
        super(message);
    }
}
