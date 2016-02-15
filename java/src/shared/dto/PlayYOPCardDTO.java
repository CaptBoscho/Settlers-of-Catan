package shared.dto;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayYOPCardDTO implements JsonSerializable {

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

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "Year_of_Plenty");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("resource1", this.resource1.toString());
        obj.addProperty("resource2", this.resource2.toString());
        return obj;
    }
}
