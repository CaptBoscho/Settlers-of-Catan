package shared.exceptions;

/**
 * Exception for trying to access a player that doesn't exist
 */
public class PlayerExistsException extends Exception {
    public PlayerExistsException(String message) {
        super(message);
    }
}
