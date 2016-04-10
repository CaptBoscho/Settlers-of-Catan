package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public final class BuildCityDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kVertexLocation = "vertexLocation";

    // -- class members
    private int playerIndex;
    private VertexLocation location;

    public BuildCityDTO(final int playerIndex, final VertexLocation location) {
        this.playerIndex = playerIndex;
        this.location = location;
    }

    public BuildCityDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.location = new VertexLocation(obj.get(kVertexLocation).getAsJsonObject());
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
        obj.addProperty(kType, "buildCity");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.add(kVertexLocation, this.location.toJSON());
        return obj;
    }
}
