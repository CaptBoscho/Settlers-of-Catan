package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.HexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class PlaySoldierCardDTO implements JsonSerializable {

    int playerIndex;
    int victimIndex;
    HexLocation location;

    /**
     *
     * @param playerIndex Who's playing this dev card
     * @param victimIndex The index of the player to rob
     * @param location    The new location of the robber
     */
    public PlaySoldierCardDTO(int playerIndex, int victimIndex, HexLocation location) {
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
        return null;
    }
}
