package shared.exceptions;

/**
 * Exception for Game being over or not
 */
public class GameOverException extends Exception {
    public GameOverException(String message) {
        super(message);
    }
}

