package shared.exceptions;

/**
 * Exception if Robber is moved to where it previously was located
 */
public class CreateAIException extends Exception {

    public CreateAIException(String error) {
        super(error);
    }

}
