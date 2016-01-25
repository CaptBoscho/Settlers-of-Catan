package shared.model.player;

import shared.exceptions.InvalidNameException;

/**
 * Representation of Player name
 *
 * @author Kyle Cornelison
 */
public class Name {
    private String name;

    /**
     * Default Constructor
     */
    public Name(){
        this.name = null;
    }

    /**
     * Name Constructor
     * @param name Value of name
     * @throws InvalidNameException
     */
    public Name(String name) throws InvalidNameException {
        this.name = name;
    }

    /**
     * Validate name value
     * @param name Desired value of name
     */
    private boolean validate(String name){
        return true;
    }

    /*===========================================
                  Getters/Setters
    ============================================*/

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidNameException {
        this.name = name;
    }
}
