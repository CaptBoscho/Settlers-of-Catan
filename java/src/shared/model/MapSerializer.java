package shared.model;

import com.google.gson.JsonObject;
import shared.model.map.*;

/**
 * Creates the JSON blob for the map
 *
 * @author Joel Bradley
 */
public class MapSerializer implements JsonSerializable {

    public MapSerializer(Map map) {

    }

    @Override
    public JsonObject toJSON() {
        JsonObject mapBlob = new JsonObject();
        return mapBlob;
    }

}
