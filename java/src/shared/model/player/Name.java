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
     * Name Constructor
     * @param name Value of name
     * @throws InvalidNameException
     */
    public Name(String name) throws InvalidNameException {
        assert name != null;
        assert name.length() > 0;

        if(isAlpha(name)) {
            this.name = name;
        } else {
            throw new InvalidNameException("The name entered is invalid!");
        }
    }

    //Helper Methods
    //===========================================

    /**
     * Tests if a string contains only alpha characters
     * @param str String to test
     * @return True if the string contains only alpha characters
     */
    private boolean isAlpha(String str) {
        assert str != null;
        
        return str.matches("[a-zA-Z]+");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Name))return false;

        Name otherName = (Name)o;
        return this.name.equals(otherName.toString());
    }

    //Getters/Setters
    //============================================

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) throws InvalidNameException {
        assert name != null;
        assert name.length() >= 0;

        this.name = name;
    }
}
