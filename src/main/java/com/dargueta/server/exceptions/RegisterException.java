package server.exceptions;

/**
 * Exception for an invalid player
 */
public class RegisterException extends Exception {
    public RegisterException(String message) {
        super(message);
    }
}
