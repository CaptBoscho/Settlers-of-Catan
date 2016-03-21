package server.exceptions;

/**
 * Exception for an invalid player
 */
public class FinishTurnException extends Exception {
    public FinishTurnException(String message) {
        super(message);
    }
}
