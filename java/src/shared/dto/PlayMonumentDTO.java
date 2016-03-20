package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayMonumentDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";

    // -- class members
    private int playerIndex;

    public PlayMonumentDTO(int playerIndex) {
        assert playerIndex >= 0;

        this.playerIndex = playerIndex;
    }

    public PlayMonumentDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "Monument");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        return obj;
    }
}
