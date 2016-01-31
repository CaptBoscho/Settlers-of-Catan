package shared.exceptions;

/**
 * Exception if the Dice Roll is invalid
 */
public class InvalidDiceRollException extends Exception {

    public InvalidDiceRollException(String error) {
        super(error);
    }

}
