package shared.dto;

import com.google.gson.JsonObject;

/**
 * @author Derek Argueta
 */
public class PlaceholderDummyDTO implements IDTO {

    // literally doesn't do anything. Danny has bad design.

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
