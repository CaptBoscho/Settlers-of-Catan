package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.definitions.ResourceType;
import shared.model.JsonSerializable;
import shared.model.bank.InvalidTypeException;

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

    public int getPlayerIndex() {
        return playerIndex;
    }

    public ResourceType getResource() throws InvalidTypeException {
        switch (resource) {
            case "brick":
                return ResourceType.BRICK;
            case "sheep":
                return ResourceType.SHEEP;
            case "WOOD":
                return ResourceType.WOOD;
            case "wheat":
                return ResourceType.WHEAT;
            case "ore":
                return ResourceType.ORE;
        }
        throw new InvalidTypeException("Invalid Resource Type in PlayMonopolyDTO");
    }
}
