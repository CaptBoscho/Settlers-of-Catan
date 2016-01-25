package shared.model.map;

/**
 * Exception if Robber is moved to where it previously was located
 */
public class AlreadyRobbedException extends Exception {

    public AlreadyRobbedException(String error) {
        super(error);
    }

}
