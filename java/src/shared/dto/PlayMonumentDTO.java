package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class PlayMonumentDTO implements JsonSerializable {

    private int playerIndex;

    public PlayMonumentDTO(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "Monument");
        obj.addProperty("playerIndex", this.playerIndex);
        return obj;
    }
}
