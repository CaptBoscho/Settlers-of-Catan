package server.exceptions;

/**
 * Exception for when the specified db plugin doesn't exist
 */
public class PluginNotFoundException extends Exception {
    public PluginNotFoundException() {
        super("Couldn't find the given plugin");
    }
}
