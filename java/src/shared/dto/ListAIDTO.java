package shared.dto;

import client.data.GameInfo;
import client.data.PlayerInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import shared.model.JsonSerializable;
import shared.model.ai.AIType;

import java.util.List;

/**
 * @author Derek Argueta
 */
public final class ListAIDTO implements IDTO,JsonSerializable {
    private List<AIType> _AIs;

    public ListAIDTO(List<AIType> types) {
        _AIs = types;
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

    /**
     * Builds a JSON array of ai types
     *
     * @return
     */
    public JsonArray toJSONArr() {

        final JsonArray allAIs = new JsonArray();
        for(final AIType aiType : this._AIs) {
            // Serialize all the ai types
            final JsonObject aiObj = new JsonObject();
            aiObj.addProperty("ai", aiType.toString());
            allAIs.add(aiObj);
        }

        return allAIs;
    }
}
