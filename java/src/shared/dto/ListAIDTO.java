package shared.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;
import shared.model.ai.aimodel.AIType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Derek Argueta
 */
public final class ListAIDTO implements IDTO,JsonSerializable {
    // -- JSON keys
    private static final String kId = "id";
    private static final String kAIs = "ais";

    private List<AIType> ais;
    private int gameId;


    public ListAIDTO(int gameId, List<AIType> types) {
        this.gameId = gameId;
        this.ais = types;
    }

    public ListAIDTO(final String json){
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.gameId = obj.get(kId).getAsInt();
        this.ais = convertJsonArrayToList(obj.get(kAIs).getAsJsonArray());
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

        final JsonArray allAIs = this.toJSONArr();
        obj.add(kAIs, allAIs);

        return obj;
    }

    /**
     * Builds a JSON array of ai types
     *
     * @return
     */
    public JsonArray toJSONArr() {

        final JsonArray allAIs = new JsonArray();
        for(final AIType aiType : this.ais) {
            // Serialize all the ai types
            final JsonObject aiObj = new JsonObject();
            aiObj.addProperty("ai", aiType.toString());
            allAIs.add(aiObj);
        }

        return allAIs;
    }

    private List<AIType> convertJsonArrayToList(final JsonArray arr){
        List<AIType> aiTypes = new ArrayList<>();

        arr.forEach(ai->
            aiTypes.add(
                AIType.valueOf(ai.getAsJsonObject().get("ai").getAsString())
            )
        );

        return aiTypes;
    }
}
