package shared.dto;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * @author Derek Argueta
 */
public class PlaceholderDummyDTO implements Serializable, IDTO {

    // literally doesn't do anything. Danny has bad design.

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
