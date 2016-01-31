package shared.model.map.hex;
import com.google.gson.JsonObject;
import shared.definitions.HexType;
import shared.locations.HexLocation;
import shared.model.JsonSerializable;

/**
 * Representation of a Hex in the map. The map is comprised of hexes. A hex has a HexLocation
 * and a HexType
 *
 * @authorn Joel Bradley
 */
public class Hex implements JsonSerializable {

    protected HexLocation hexLoc;
    protected HexType hexType;

    /**
     * Default constructor for a Hex
     * @param hexLoc HexLocation
     * @param hexType HexType
     */
    public Hex(HexLocation hexLoc, HexType hexType) {
        this.hexLoc = hexLoc;
        this.hexType = hexType;
    }

    /**
     * Constructs a Hex object from JSON
     * @param json The JSON representation of the object
     */
    public Hex(JsonObject json) {

    }

    /**
     * Converts the object to JSON
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }

    public HexLocation getLocation() {
        return hexLoc;
    }

    public HexType getType() {
        return hexType;
    }

}
