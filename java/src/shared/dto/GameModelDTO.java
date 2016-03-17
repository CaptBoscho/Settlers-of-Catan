package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * Created by Kyle 'TMD' Cornelison on 3/16/2016.
 */
public class GameModelDTO implements IDTO,JsonSerializable {
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
