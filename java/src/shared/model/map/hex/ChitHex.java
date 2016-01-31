package shared.model.map.hex;

import com.google.gson.JsonObject;
import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * A ChitHex is a Hex that has a chit
 *
 * @author Joel Bradley
 */
public class ChitHex extends Hex {

    private int chit;

    /**
     * Default constructor for a ChitHex
     * @param hexLoc HexLocation
     * @param hexType HexType
     * @param chit int
     */
    public ChitHex(HexLocation hexLoc, HexType hexType, int chit) {
        super(hexLoc, hexType);
        this.chit = chit;
    }

    /**
     * Constructs a ChitHex object from JSON
     * @param json The JSON representation of the object
     */
    public ChitHex(JsonObject json) {
        super(json);
    }

    /**
     * Converts the object to JSON
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }

    public int getChit() {
        return chit;
    }

}
