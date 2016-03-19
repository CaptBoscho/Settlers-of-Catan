package shared.dto;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.model.JsonSerializable;

/**
 * Transport object for joining a game
 *
 * @author Derek Argueta
 */
public final class JoinGameDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kId = "id";
    private static final String kColor = "color";

    private int gameId;
    private CatanColor color;

    public JoinGameDTO(final int gameId, final CatanColor color) {
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
        final JsonObject obj = new JsonObject();
        obj.addProperty(kId, this.gameId);
        obj.addProperty(kColor, this.color.toString().toLowerCase());
        return obj;
    }
}
