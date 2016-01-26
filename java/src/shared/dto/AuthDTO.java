package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * A data-transport-object to pass authentication credentials over the internet
 *
 * @author Derek Argueta
 */
public class AuthDTO implements JsonSerializable{

    private String username;
    private String password;

    AuthDTO(String username, String password) {
        assert username != null;
        assert password != null;
        this.username = username;
        this.password = password;
    }


    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("username", this.username);
        json.addProperty("password", this.password);
        return json;
    }
}
