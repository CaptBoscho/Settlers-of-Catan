package client.services;

/**
 * @author Derek Argueta
 */
public final class MissingUserCookieException extends Throwable {

    public MissingUserCookieException(String message) {
        super(message);
    }
}
