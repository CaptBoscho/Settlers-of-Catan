package shared.model.map;


import shared.locations.HexLocation;

/**
 * Robber class representing the Robber piece in a game
 */
public class Robber {
    private HexLocation location;

    /**
     * Default Constructor
     */
    public Robber(){
        this.location = new HexLocation(0,0);
    }

    /**
     * Overloaded Constructor
     * @param location HexLocation of the Robber
     */
    public Robber(HexLocation location){
        this.location = location;
    }

    /*===========================================
                   Getters/Setters
     ============================================*/
    public HexLocation getLocation() {
        return location;
    }

    public void setLocation(HexLocation location) {
        this.location = location;
    }
}
