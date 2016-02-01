package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class RollNumberDTO implements JsonSerializable {

    private int playerIndex;
    private int numberRolled;

    public RollNumberDTO(int playerIndex, int numberRolled) {
        this.playerIndex = playerIndex;
        this.numberRolled = numberRolled;
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