package client.services.exceptions;

/**
 * @author Derek Argueta
 */
public class BadRequestException extends BadHttpRequestException {
    public BadRequestException() {
        super("Received Bad Request error (400)");
    }
}
