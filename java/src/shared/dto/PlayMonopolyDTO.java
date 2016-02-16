package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayMonopolyDTO implements JsonSerializable {

    private int playerIndex;
    private String resource;

    public PlayMonopolyDTO(int playerIndex, String resource) {
        assert playerIndex >= 0;
        assert resource != null;
        assert resource.length() > 0;

        this.playerIndex = playerIndex;
        this.resource = resource;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "Monopoly");
        obj.addProperty("resource", this.resource);
        obj.addProperty("playerIndex", this.playerIndex);
        return obj;
    }
}
