package shared.dto;

import com.google.gson.JsonObject;
import shared.locations.HexLocation;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlaySoldierCardDTO implements IDTO,JsonSerializable {

    private int playerIndex;
    private int victimIndex;
    private HexLocation location;

    /**
     *
     * @param playerIndex Who's playing this dev card
     * @param victimIndex The index of the player to rob
     * @param location    The new location of the robber
     */
    public PlaySoldierCardDTO(int playerIndex, int victimIndex, HexLocation location) {
        assert playerIndex >= 0;
        assert victimIndex >= 0;
        assert location != null;

        this.playerIndex = playerIndex;
        this.victimIndex = victimIndex;
        this.location = location;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getVictimIndex() {
        return victimIndex;
    }

    public HexLocation getLocation() {
        return location;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "Soldier");
        obj.addProperty("playerIndex", this.playerIndex);
        obj.addProperty("victimIndex", this.victimIndex);
        obj.add("location", this.location.toJSON());
        return obj;
    }
}
