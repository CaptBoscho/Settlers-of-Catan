package client.services.exceptions;

/**
 * @author Derek Argueta
 */
public class UnauthorizedException extends BadHttpRequestException {
    public UnauthorizedException() {
        super("Unauthorized error. Your cookies are not getting recognized by the server");
    }
}
