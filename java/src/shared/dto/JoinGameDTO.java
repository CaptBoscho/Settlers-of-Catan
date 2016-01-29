package shared.dto;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.model.JsonSerializable;

/**
 * Transport object for joining a game
 *
 * @author Derek Argueta
 */
public class JoinGameDTO implements JsonSerializable {

    private int gameId;
    private CatanColor color;

    public JoinGameDTO(int gameId, CatanColor color) {
        assert gameId >= 0;
        assert color != null;
        this.gameId = gameId;
        this.color = color;
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
