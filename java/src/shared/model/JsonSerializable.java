package shared.model;

import com.google.gson.JsonObject;

/**
 * An interface that guarantees that an object has a toJSON method.
 *
 * @author Derek Argueta
 */
public interface JsonSerializable {

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    JsonObject toJSON();
}
