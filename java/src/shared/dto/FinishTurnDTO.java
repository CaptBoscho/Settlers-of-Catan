package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class FinishTurnDTO implements JsonSerializable {

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
        return null;
    }
}
