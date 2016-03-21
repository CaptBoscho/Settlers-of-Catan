package client.services.exceptions;

/**
 * @author Derek Argueta
 */
public class BadHttpRequestException extends Exception {
    public BadHttpRequestException(final String message) {
        super(message);
    }
}
