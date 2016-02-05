package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class PlayMonopolyDTO implements JsonSerializable {

    private int playerIndex;
    private String resource;

    public PlayMonopolyDTO(int playerIndex, String resource) {
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
        // TODO --
        return null;
    }
}