package shared.dto;

import com.google.gson.JsonObject;
import shared.model.JsonSerializable;

/**
 * Transport object for the create game HTTP request
 *
 * @author Derek Argueta
 */
public class CreateGameDTO implements JsonSerializable{

    private boolean randomTiles;
    private boolean randomNumbers;
    private boolean randomPorts;
    private String name;

    public CreateGameDTO(boolean rt, boolean rn, boolean rp, String n) {
        assert name != null;
        this.randomTiles = rt;
        this.randomNumbers = rn;
        this.randomPorts = rp;
        this.name = n;
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
