package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class ChangeLogLevelDTO implements JsonSerializable {

    private String logLevel;

    public ChangeLogLevelDTO(String logLevel) {
        assert logLevel != null;
        assert logLevel.length() > 0;

        this.logLevel = logLevel;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject obj = new JsonObject();
        obj.addProperty("logLevel", this.logLevel);
        return obj;
    }
}
