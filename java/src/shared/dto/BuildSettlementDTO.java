package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuildSettlementDTO implements IDTO,JsonSerializable {

    private int playerIndex;
    private VertexLocation location;
    private boolean free;

    public BuildSettlementDTO(int playerIndex, VertexLocation location, boolean free) {
        assert playerIndex >= 0;
        assert location != null;

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
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "buildSettlement");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("free", this.free);
        obj.add("vertexLocation", this.location.toJSON());
        return obj;
    }
}
