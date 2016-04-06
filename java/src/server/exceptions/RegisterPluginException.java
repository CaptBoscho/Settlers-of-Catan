package server.exceptions;

/**
 * Exception for failing to register a plugin
 */
public class RegisterPluginException extends Exception {
    public RegisterPluginException(String message) {
        super(message);
    }
}
