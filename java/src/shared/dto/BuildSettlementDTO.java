package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuildSettlementDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kFree = "free";
    private static final String kVertexLocation = "vertexLocation";

    private int playerIndex;
    private VertexLocation location;
    private boolean free;

    public BuildSettlementDTO(final int playerIndex, final VertexLocation location, final boolean free) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert location != null;

        this.playerIndex = playerIndex;
        this.location = location;
        this.free = free;
    }

    public BuildSettlementDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.location = new VertexLocation(obj.get(kVertexLocation).getAsJsonObject());
        this.free = obj.get(kFree).getAsBoolean();
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public VertexLocation getLocation() {
        return location;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "buildSettlement");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kFree, this.free);
        obj.add(kVertexLocation, this.location.toJSON());
        return obj;
    }
}
