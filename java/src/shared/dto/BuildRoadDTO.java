package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.EdgeLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuildRoadDTO implements JsonSerializable {

    private int playerIndex;
    private EdgeLocation roadLocation;
    private boolean free;

    public BuildRoadDTO(int playerIndex, EdgeLocation location, boolean free) {
        assert playerIndex >= 0;
        assert location != null;

        this.playerIndex = playerIndex;
        this.roadLocation = location;
        this.free = free;
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
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "buildRoad");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("free", this.free);
        obj.add("roadLocation", this.roadLocation.toJSON());
        return obj;
    }
}
