package shared.exceptions;

/**
 * Exception used for deserialization, to be thrown when the JSON has invalid values.
 *
 * @author Danny Harding
 */
public class BadJsonException extends Exception {
    public BadJsonException(String s) {
        super(s);
    }
}
