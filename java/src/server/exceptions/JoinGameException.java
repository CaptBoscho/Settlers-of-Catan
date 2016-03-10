package server.exceptions;

/**
 * Exception for an invalid player
 */
public class JoinGameException extends Exception {
    public JoinGameException(String message) {
        super(message);
    }
}
