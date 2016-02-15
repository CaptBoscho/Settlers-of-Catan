package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class FinishTurnDTO implements JsonSerializable {

    private int playerIndex;

    /**
     *
     * @param playerIndex Who's sending this command (0-3)
     */
    public FinishTurnDTO(int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex <= 3;

        this.playerIndex = playerIndex;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "finishTurn");
        obj.addProperty("playerIndex", this.playerIndex);
        return obj;
    }
}
