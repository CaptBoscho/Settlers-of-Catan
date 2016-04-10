package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.locations.EdgeLocation;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * @author Joel Bradley
 */
public final class RoadBuildingDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kSpotOne = "spot1";
    private static final String kSpotTwo = "spot2";

    // -- class members
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

    public RoadBuildingDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.roadLocationOne = new EdgeLocation(obj.get(kSpotOne).getAsJsonObject());
        this.roadLocationTwo = new EdgeLocation(obj.get(kSpotTwo).getAsJsonObject());
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public EdgeLocation getRoadLocationOne() {
        return roadLocationOne;
    }

    public EdgeLocation getRoadLocationTwo() {
        return roadLocationTwo;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "Road_Building");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.add(kSpotOne, this.roadLocationOne.toJSON());
        obj.add(kSpotTwo, this.roadLocationTwo.toJSON());
        return obj;
    }
}
