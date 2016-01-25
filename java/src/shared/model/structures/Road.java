package shared.model.structures;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Danny Harding
 */
public class Road implements JsonSerializable {
    int playerID;

    /**
     * Construct a Road object from a JSON blob
     *
     * @param json The JSON being used to construct this object
     */
    public Road(JsonObject json) {

    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
