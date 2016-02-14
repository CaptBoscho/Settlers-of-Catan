package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class SaveGameDTO implements JsonSerializable {

    private int gameId;
    private String name;

    public SaveGameDTO(int gameId, String name) {
        assert gameId >= 0;
        assert name != null;
        assert name.length() > 0;

        this.gameId = gameId;
        this.name = name;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", this.gameId);
        obj.addProperty("name", this.name);
        return obj;
    }
}
