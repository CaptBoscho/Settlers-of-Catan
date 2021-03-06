package shared.model.map;


import shared.locations.HexLocation;

import java.io.Serializable;

/**
 * Robber class representing the Robber piece in a game.
 *
 * @author Joel Bradley
 */
public final class Robber implements Serializable {
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
        assert hexLoc != null;

        this.hexLoc = hexLoc;
    }

    public HexLocation getLocation() {
        return hexLoc;
    }

    public void setLocation(final HexLocation hexLoc) {
        assert hexLoc != null;

        this.hexLoc = hexLoc;
    }
}
