package shared.model.player;

/**
 * Representation of Player name
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
     * Name constructor
     * @param name Value of name
     */
    public Name(String name){
        this.name = name;
    }

    /**
     * Validate name value
     * @param name Desired value of name
     */
    public boolean validate(String name){
        return true;
    }

    /*===========================================
                  Getters/Setters
    ============================================*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
