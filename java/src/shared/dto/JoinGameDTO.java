package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public JoinGameDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.gameId = obj.get(kId).getAsInt();
        this.color = CatanColor.translateFromString(obj.get(kColor).getAsString());
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

    public static boolean isValidRequestJson(String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasId = obj.has(kId) && obj.get(kId).isJsonPrimitive();
        final boolean hasColor = obj.has(kColor) && obj.get(kColor).isJsonPrimitive();

        return hasId && hasColor;
    }
}
