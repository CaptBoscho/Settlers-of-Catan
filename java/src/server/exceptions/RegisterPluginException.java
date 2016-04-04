package server.exceptions;

/**
 * Exception for an invalid player
 */
public class RegisterPluginException extends Exception {
    public RegisterPluginException(String message) {
        super(message);
    }
}
