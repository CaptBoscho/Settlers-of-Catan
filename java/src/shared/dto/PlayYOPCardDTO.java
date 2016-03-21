package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class PlayYOPCardDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kResource1 = "resource1";
    private static final String kResource2 = "resource2";

    // -- class members
    private int playerIndex;
    private ResourceType resource1;
    private ResourceType resource2;

    public PlayYOPCardDTO(int playerIndex, ResourceType resource1, ResourceType resource2) {
        assert playerIndex >= 0;
        assert resource1 != null;
        assert resource2 != null;

        this.playerIndex = playerIndex;
        this.resource1 = resource1;
        this.resource2 = resource2;
    }

    public PlayYOPCardDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.resource1 = ResourceType.translateFromString(obj.get(kResource1).getAsString());
        this.resource2 = ResourceType.translateFromString(obj.get(kResource2).getAsString());
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "Year_of_Plenty");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kResource1, this.resource1.toString().toLowerCase());
        obj.addProperty(kResource2, this.resource2.toString().toLowerCase());
        return obj;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public ResourceType getResource1() {
        return resource1;
    }

    public ResourceType getResource2() {
        return resource2;
    }

    public static boolean isValidRequestJson(final String json) {
        if(!JSONUtils.isJSONValid(json)) {
            return false;
        }
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasResource1 = obj.has(kResource1) && obj.get(kResource1).isJsonPrimitive();
        final boolean hasResource2 = obj.has(kResource2) && obj.get(kResource2).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasResource1 && hasResource2;
    }
}
