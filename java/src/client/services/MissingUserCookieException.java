package client.services;

/**
 * @author Derek Argueta
 */
public class MissingUserCookieException extends Throwable {

    public MissingUserCookieException(String message) {
        super(message);
    }
}
