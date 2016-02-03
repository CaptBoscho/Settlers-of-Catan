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
        if(validate(name))
            this.name = name;
        else
            throw new InvalidNameException("The name entered is invalid!");
    }

    /**
     * Validate name value
     * @param name Desired value of name
     */
    private boolean validate(String name){
        if(isAlpha(name))
            return true;
        else
            return false;
    }

    //Helper Methods
    //===========================================

    /**
     * Tests if a string contains only alpha characters
     * @param str String to test
     * @return True if the string contains only alpha characters
     */
    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    //Getters/Setters
    //============================================

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) throws InvalidNameException {
        this.name = name;
    }
}
