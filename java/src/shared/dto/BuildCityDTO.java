package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class BuildCityDTO implements IDTO, JsonSerializable {

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
