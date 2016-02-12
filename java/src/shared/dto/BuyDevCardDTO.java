package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class BuyDevCardDTO implements JsonSerializable {

    private int playerIndex;

    public BuyDevCardDTO(int playerIndex) {
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
        obj.addProperty("type", "buyDevCard");
        obj.addProperty("playerIndex", this.playerIndex);
        return obj;
    }
}
