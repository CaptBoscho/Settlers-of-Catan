package server.exceptions;

/**
 * Exception for an invalid player
 */
public class CreateGameException extends Exception {
    public CreateGameException(String message) {
        super(message);
    }
}
