package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.EdgeLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class BuildRoadDTO implements JsonSerializable {

    private int playerIndex;
    private EdgeLocation roadLocation;
    private boolean free;

    public BuildRoadDTO(int playerIndex, EdgeLocation location, boolean free) {
        this.playerIndex = playerIndex;
        this.roadLocation = location;
        this.free = free;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        // TODO --
        return null;
    }
}
