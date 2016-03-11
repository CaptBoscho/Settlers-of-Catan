package server.exceptions;

/**
 * Exception for an invalid player
 */
public class SendChatException extends Exception {
    public SendChatException(String message) {
        super(message);
    }
}
