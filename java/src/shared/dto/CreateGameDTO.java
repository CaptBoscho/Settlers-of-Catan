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
        assert n != null;
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
        JsonObject obj = new JsonObject();
        obj.addProperty("randomTiles", this.randomTiles);
        obj.addProperty("randomNumbers", this.randomNumbers);
        obj.addProperty("randomPorts", this.randomPorts);
        obj.addProperty("name", this.name);
        return obj;
    }
}
