package shared.model.map;


import shared.locations.HexLocation;

/**
 * Robber class representing the Robber piece in a game.
 *
 * @author Joel Bradley
 */
public class Robber {
    private HexLocation hexLoc;

    /**
     * Default Constructor
     */
    public Robber() {
        this.hexLoc = new HexLocation(0, 0);
    }

    /**
     * Overloaded Constructor
     * @param hexLoc HexLocation of the Robber
     */
    public Robber(HexLocation hexLoc) {
        this.hexLoc = hexLoc;
    }

    public HexLocation getLocation() {
        return hexLoc;
    }

    public void setLocation(HexLocation hexLoc) {
        this.hexLoc = hexLoc;
    }
}
