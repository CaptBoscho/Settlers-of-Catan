package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.HexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class RobPlayerDTO implements JsonSerializable {

    private int playerIndex;
    private int victimIndex;
    private HexLocation location;

    public RobPlayerDTO(int playerIndex, int victimIndex, HexLocation location) {
        assert playerIndex >= 0;
        assert victimIndex >= 0;
        assert playerIndex != victimIndex;
        assert location != null;

        this.playerIndex = playerIndex;
        this.victimIndex = victimIndex;
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
        obj.addProperty("type", "robPlayer");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("victimIndex", this.victimIndex);
        obj.add("location", this.location.toJSON());
        return obj;
    }
}
