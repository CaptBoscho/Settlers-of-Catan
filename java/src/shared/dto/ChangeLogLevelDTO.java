package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public final class ChangeLogLevelDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kLogLevel = "logLevel";

    // -- class members
    private String logLevel;

    public ChangeLogLevelDTO(final String logLevel) {
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
        final JsonObject obj = new JsonObject();
        obj.addProperty(kLogLevel, this.logLevel);
        return obj;
    }
}
