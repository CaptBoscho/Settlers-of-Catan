package client.services.exceptions;

/**
 * @author Derek Argueta
 */
public class InternalServerErrorException extends BadHttpRequestException {

    public InternalServerErrorException() {
        super("Recieved internal server error (500)");
    }
}
