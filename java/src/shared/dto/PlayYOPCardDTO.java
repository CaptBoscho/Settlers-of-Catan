package shared.dto;

import com.google.gson.JsonObject;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class PlayYOPCardDTO implements JsonSerializable {

    private int playerIndex;
    private ResourceType resource1;
    private ResourceType resource2;

    public PlayYOPCardDTO(int playerIndex, ResourceType resource1, ResourceType resource2) {
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
        return null;
    }
}
