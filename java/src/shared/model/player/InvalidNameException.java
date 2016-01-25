package shared.model.player;

/**
 * Exception for invalid player names
 */
public class InvalidNameException extends Exception {
    public InvalidNameException(String message) {
        super(message);
    }
}

