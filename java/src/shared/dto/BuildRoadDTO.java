package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.locations.EdgeLocation;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public final class BuildRoadDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kFree = "free";
    private static final String kRoadLocation = "roadLocation";

    // -- class members
    private int playerIndex;
    private EdgeLocation roadLocation;
    private boolean free;

    public BuildRoadDTO(final int playerIndex, final EdgeLocation location, final boolean free) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert location != null;

        this.playerIndex = playerIndex;
        this.roadLocation = location;
        this.free = free;
    }

    public BuildRoadDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.free = obj.get(kFree).getAsBoolean();
        this.roadLocation = new EdgeLocation(obj.get(kRoadLocation).getAsJsonObject());
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public EdgeLocation getRoadLocation() {
        return roadLocation;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "buildRoad");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kFree, this.free);
        obj.add(kRoadLocation, this.roadLocation.toJSON());
        return obj;
    }
}
