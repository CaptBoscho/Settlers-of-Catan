package server.exceptions;

/**
 * Exception for when the specified db plugin doesn't exist
 */
public class PluginExistsException extends Exception {
    public PluginExistsException(String message) {
        super(message);
    }
}
