package server.exceptions;

/**
 * Exception for an invalid player
 */
public class PluginExistsException extends Exception {
    public PluginExistsException(String message) {
        super(message);
    }
}
