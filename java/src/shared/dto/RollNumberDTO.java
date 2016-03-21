package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class RollNumberDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kPlayerIndex = "playerIndex";
    private static final String kNumber = "number";

    // -- class members
    private int playerIndex;
    private int numberRolled;

    public RollNumberDTO(int playerIndex, int numberRolled) {
        assert playerIndex >= 0;
        assert numberRolled > 0;

        this.playerIndex = playerIndex;
        this.numberRolled = numberRolled;
    }

    public RollNumberDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.playerIndex = obj.get(kPlayerIndex).getAsInt();
        this.numberRolled = obj.get(kNumber).getAsInt();
    }

    public int getValue(){
        return this.numberRolled;
    }

    public int getPlayerIndex(){
        return this.playerIndex;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "rollNumber");
        obj.addProperty(kPlayerIndex, this.playerIndex);
        obj.addProperty(kNumber, this.numberRolled);
        return obj;
    }

    public static boolean isValidRequestJson(final String json) {
        if(!JSONUtils.isJSONValid(json)) {
            return false;
        }
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasPlayerIndex = obj.has(kPlayerIndex) && obj.get(kPlayerIndex).isJsonPrimitive();
        final boolean hasNumber = obj.has(kNumber) && obj.get(kNumber).isJsonPrimitive();

        return hasType && hasPlayerIndex && hasNumber;
    }
}
