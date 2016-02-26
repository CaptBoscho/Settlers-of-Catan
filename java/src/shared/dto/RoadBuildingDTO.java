package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.EdgeLocation;
import shared.model.JsonSerializable;

/**
 * @author Joel Bradley
 */
public final class RoadBuildingDTO implements JsonSerializable {

    private int playerIndex;
    private EdgeLocation roadLocationOne;
    private EdgeLocation roadLocationTwo;

    public RoadBuildingDTO(int playerIndex, EdgeLocation locationOne, EdgeLocation locationTwo) {
        assert playerIndex >= 0;
        assert locationOne != null;
        assert locationTwo != null;

        this.playerIndex = playerIndex;
        this.roadLocationOne = locationOne;
        this.roadLocationTwo = locationTwo;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "Road_Building");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.add("spot1", this.roadLocationOne.toJSON());
        obj.add("spot2", this.roadLocationTwo.toJSON());
        return obj;
    }
}
