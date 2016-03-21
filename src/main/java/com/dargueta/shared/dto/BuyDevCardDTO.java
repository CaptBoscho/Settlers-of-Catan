package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuyDevCardDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";

    // -- class members
    private int playerIndex;

    public BuyDevCardDTO(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        this.playerIndex = playerIndex;
    }

    public BuyDevCardDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "buyDevCard");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        return obj;
    }
}
