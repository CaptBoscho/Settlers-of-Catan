package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * @author Derek Argueta
 */
public class LoadGameDTO implements JsonSerializable {

    private String name;

    public LoadGameDTO(String name) {
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
        return null;
    }
}
