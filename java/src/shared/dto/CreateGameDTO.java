package shared.dto;

import com.google.gson.JsonObject;
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
}
