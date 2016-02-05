package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class BuildSettlementDTO implements JsonSerializable {

    private int playerIndex;
    private VertexLocation location;
    private boolean free;

    public BuildSettlementDTO(int playerIndex, VertexLocation location, boolean free) {
        this.playerIndex = playerIndex;
        this.location = location;
        this.free = free;
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
