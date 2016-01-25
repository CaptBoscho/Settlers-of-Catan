package shared.exceptions;

/**
 * Exception if a structure is attempted to be built when it is not allowed
 */
public class StructureException extends Exception {

    public StructureException(String error) {
        super(error);
    }

}
