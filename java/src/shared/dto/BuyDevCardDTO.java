package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class BuyDevCardDTO implements JsonSerializable {

    private int playerIndex;

    public BuyDevCardDTO(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        // TODO --
        return null;
    }
}
