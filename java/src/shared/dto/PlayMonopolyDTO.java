package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayMonopolyDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kResource = "resource";
    private static final String kPlayerIndex = "playerIndex";

    // -- class members
    private int playerIndex;
    private String resource;

    public PlayMonopolyDTO(final int playerIndex, final String resource) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert resource != null;
        assert resource.length() > 0;

        this.playerIndex = playerIndex;
        this.resource = resource;
    }

    public PlayMonopolyDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.resource = obj.get(kResource).getAsString();
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kType, "Monopoly");
        obj.addProperty(kResource, this.resource.toLowerCase());
        obj.addProperty(kPlayerIndex, this.playerIndex);
        return obj;
    }

    public static boolean isValidRequestJson(String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasResource = obj.has(kResource) && obj.get(kResource).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();

        return hasType && hasResource && hasPlayerIndex;
    }
}
