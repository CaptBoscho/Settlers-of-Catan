package shared.exceptions;

/**
 * Exception if an invalid location is passed into the map
 */
public class InvalidLocationException extends Exception {

    public InvalidLocationException(String error) {
        super(error);
    }

}
