package shared.model.map.hex;


import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * Representation of a Hex in the map. The map is comprised of hexes. A hex has a HexLocation
 * and a HexType
 *
 * @author Joel Bradley
 */
public class Hex {

    protected HexLocation hexLoc;
    protected HexType hexType;

    /**
     * Default constructor for a Hex
     * @param hexLoc HexLocation
     * @param hexType HexType
     */
    public Hex(HexLocation hexLoc, HexType hexType) {
        assert hexLoc != null;
        assert hexType != null;

        this.hexLoc = hexLoc;
        this.hexType = hexType;
    }

    public HexLocation getLocation() {
        return hexLoc;
    }

    public HexType getType() {
        return hexType;
    }

}