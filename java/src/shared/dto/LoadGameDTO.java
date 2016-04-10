package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public final class LoadGameDTO implements Serializable, IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kName = "name";

    // -- class members
    private String name;

    public LoadGameDTO(final String name) {
        assert name != null;
        assert name.length() > 0;

        this.name = name;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kName, this.name);
        return obj;
    }
}
