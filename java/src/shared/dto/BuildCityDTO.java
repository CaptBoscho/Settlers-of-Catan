package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuildCityDTO implements IDTO,JsonSerializable {

    private int playerIndex;
    private VertexLocation location;

    public BuildCityDTO(int playerIndex, VertexLocation location) {
        this.playerIndex = playerIndex;
        this.location = location;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "buildCity");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.add("vertexLocation", this.location.toJSON());
        return obj;
    }
}
