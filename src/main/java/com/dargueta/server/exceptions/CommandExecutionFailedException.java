package server.exceptions;

/**
 * Exception for a failed command exception
 */
public class CommandExecutionFailedException extends Exception {
    public CommandExecutionFailedException(String message) {
        super(message);
    }
}