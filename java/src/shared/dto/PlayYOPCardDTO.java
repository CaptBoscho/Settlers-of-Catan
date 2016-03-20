package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayYOPCardDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kResourceOne = "resource1";
    private static final String kResourceTwo = "resource2";

    // -- class members
    private int playerIndex;
    private ResourceType resource1;
    private ResourceType resource2;

    public PlayYOPCardDTO(int playerIndex, ResourceType resource1, ResourceType resource2) {
        assert playerIndex >= 0;
        assert resource1 != null;
        assert resource2 != null;

        this.playerIndex = playerIndex;
        this.resource1 = resource1;
        this.resource2 = resource2;
    }

    public PlayYOPCardDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.resource1 = ResourceType.translateFromString(obj.get(kResourceOne).getAsString());
        this.resource2 = ResourceType.translateFromString(obj.get(kResourceTwo).getAsString());
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "Year_of_Plenty");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kResourceOne, this.resource1.toString().toLowerCase());
        obj.addProperty(kResourceTwo, this.resource2.toString().toLowerCase());
        return obj;
    }
}
