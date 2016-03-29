package shared.exceptions;

/**
 * @author Danny Harding
 */
public class BadCallerException extends Throwable {
    public BadCallerException(String s) {
        super(s);
    }
}
