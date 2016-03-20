package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.JsonSerializable;

/**
 * A data-transport-object to pass authentication credentials over the internet
 *
 * @author Derek Argueta
 */
public final class AuthDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kUsername = "username";
    private static final String kPassword = "password";

    // -- class members
    private String username;
    private String password;

    /**
     * Default constructor
     *
     * @param username
     * @param password
     */
    public AuthDTO(final String username, final String password) {
        assert username != null;
        assert password != null;
        assert username.length() > 0;
        assert password.length() > 0;

        this.username = username;
        this.password = password;
    }

    public AuthDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.username = obj.get(kUsername).getAsString();
        this.password = obj.get(kPassword).getAsString();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }


    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject json = new JsonObject();
        json.addProperty(kUsername, this.username);
        json.addProperty(kPassword, this.password);
        return json;
    }

    public static boolean isValidRequestJson(String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasUsername = obj.has(kUsername) && obj.get(kUsername).isJsonPrimitive();
        final boolean hasPassword = obj.has(kPassword) && obj.get(kPassword).isJsonPrimitive();

        return hasUsername && hasPassword;
    }
}
