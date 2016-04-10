package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.locations.HexLocation;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public final class PlaySoldierCardDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kVictimIndex = "victimIndex";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kLocation = "location";

    // -- class members
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

    public PlaySoldierCardDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.victimIndex = obj.get(kVictimIndex).getAsInt();
        this.location = new HexLocation(obj.get(kLocation).getAsJsonObject());
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
        obj.addProperty(kType, "Soldier");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kVictimIndex, this.victimIndex);
        obj.add(kLocation, this.location.toJSON());
        return obj;
    }
}
