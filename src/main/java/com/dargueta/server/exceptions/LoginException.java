package server.exceptions;

/**
 * Exception for an invalid player
 */
public class LoginException extends Exception {
    public LoginException(String message) {
        super(message);
    }
}
