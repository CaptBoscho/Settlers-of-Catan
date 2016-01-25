package shared.exceptions;

/**
 * Exception for trying to access a player that doesn't exist
 */
public class PlayerExistException extends Exception {
    public PlayerExistException(String message) {
        super(message);
    }
}
