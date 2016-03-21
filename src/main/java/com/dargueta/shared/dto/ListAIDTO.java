package com.dargueta.shared.dto;

import com.google.gson.JsonObject;
import com.dargueta.shared.model.JsonSerializable;
import com.dargueta.shared.model.ai.AIType;

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
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "listAI");
        obj.addProperty("list", _AIs.toString());
        return obj;
    }
}
