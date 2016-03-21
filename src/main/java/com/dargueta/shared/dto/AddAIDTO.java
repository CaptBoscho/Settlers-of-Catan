package com.dargueta.shared.dto;

import com.google.gson.JsonObject;
import com.dargueta.shared.model.JsonSerializable;
import com.dargueta.shared.model.ai.AIType;

/**
 * @author Derek Argueta
 */
public final class AddAIDTO implements IDTO, JsonSerializable {
    private AIType type;

    public AddAIDTO(AIType type) {
        this.type = type;
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
        obj.addProperty("type", "addAI");
        obj.addProperty("ai", this.type.toString());
        return obj;
    }
}
