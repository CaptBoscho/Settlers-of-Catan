package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class SaveGameDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kId = "id";
    private static final String kName = "name";

    private int gameId;
    private String name;

    public SaveGameDTO(final int gameId, final String name) {
        assert gameId >= 0;
        assert name != null;
        assert name.length() > 0;

        this.gameId = gameId;
        this.name = name;
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
        obj.addProperty(kName, this.name);
        return obj;
    }
}
