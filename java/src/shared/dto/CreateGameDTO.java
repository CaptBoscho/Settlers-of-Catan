package shared.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.model.JsonSerializable;

/**
 * Transport object for the create game HTTP request
 *
 * @author Derek Argueta
 */
public final class CreateGameDTO implements IDTO, JsonSerializable {

    // -- JSON keys
    private static final String kRandomTiles = "randomTiles";
    private static final String kRandomNumbers = "randomNumbers";
    private static final String kRandomPorts = "randomPorts";
    private static final String kName = "name";

    // -- class members
    private boolean randomHexes;
    private boolean randomNumbers;
    private boolean randomPorts;
    private String name;

    public CreateGameDTO(final boolean randomHexes, final boolean rn, final boolean rp, final String name) {
        assert name != null;

        this.randomHexes = randomHexes;
        this.randomNumbers = rn;
        this.randomPorts = rp;
        this.name = name;
    }

    public CreateGameDTO(final String json) {
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        this.randomHexes = obj.get(kRandomTiles).getAsBoolean();
        this.randomNumbers = obj.get(kRandomNumbers).getAsBoolean();
        this.randomPorts = obj.get(kRandomPorts).getAsBoolean();
        this.name = obj.get(kName).getAsString();
    }

    public boolean isRandomTiles() {
        return this.randomHexes;
    }

    public boolean isRandomNumbers() {
        return this.randomNumbers;
    }

    public boolean isRandomPorts() {
        return this.randomPorts;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        final JsonObject obj = new JsonObject();
        obj.addProperty(kRandomTiles, this.randomHexes);
        obj.addProperty(kRandomNumbers, this.randomNumbers);
        obj.addProperty(kRandomPorts, this.randomPorts);
        obj.addProperty(kName, this.name);
        return obj;
    }

    public static boolean isValidRequestJson(String json) {
        if(!JSONUtils.isJSONValid(json)) {
            return false;
        }
        final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        final boolean hasRandomTiles = obj.has(kRandomTiles) && obj.get(kRandomTiles).isJsonPrimitive();
        final boolean hasRandomNumbers = obj.has(kRandomNumbers) && obj.get(kRandomNumbers).isJsonPrimitive();
        final boolean hasRandomPorts = obj.has(kRandomPorts) && obj.get(kRandomPorts).isJsonPrimitive();
        final boolean hasName = obj.has(kName) && obj.get(kName).isJsonPrimitive();

        return hasRandomTiles && hasRandomNumbers && hasRandomPorts && hasName;
    }
}
