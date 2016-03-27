package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.model.JsonSerializable;
import shared.model.ai.aimodel.AIType;

/**
 * @author Derek Argueta
 */
public final class AddAIDTO implements IDTO,JsonSerializable {

    // -- JSON keys
    private static final String kType = "type";
    private static final String kAi = "ai";

    private AIType type;

    public AddAIDTO(AIType type) {
        this.type = type;
    }

    public AddAIDTO(final String json) {
        try{
            this.type = AIType.valueOf(json);
        }catch(IllegalArgumentException e){
            final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            this.type = AIType.valueOf(obj.get(kAi).getAsString());
        }
    }

    public AIType getAIType(){
        return this.type;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty(kType, "addAI");
        obj.addProperty(kAi, this.type.toString());
        return obj;
    }

    public static boolean isValidRequestJson(final String json) {
        if(!JSONUtils.isJSONValid(json)) {
            return false;
        }
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasType = obj.has(kType) && obj.get(kType).isJsonPrimitive();
        final boolean hasAi = obj.has(kAi) && obj.get(kAi).isJsonPrimitive();

        return hasType && hasAi;
    }
}
