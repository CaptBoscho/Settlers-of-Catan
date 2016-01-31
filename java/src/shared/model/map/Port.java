package shared.model.map;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * Representation of Port in the game. A port is a special structure that players can interact with to trade certain
 * resources for others depending on the implemented port.
 *
 * @author Joel Bradley
 */
public class Port implements JsonSerializable {

    private PortType portType;
    private VertexLocation vertexLoc;

    /**
     * Default Constructor
     * @param portType PortType
     * @param vertexLoc VertexLocation
     */
    public Port(PortType portType, VertexLocation vertexLoc) {
        this.portType = portType;
        this.vertexLoc = vertexLoc;
    }

    /**
     * Construct a Port object from a JSON blob
     * @param json The JSON representing the object
     */
    public Port(JsonObject json) {

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

    public PortType getPortType() {
        return portType;
    }

    public VertexLocation getVertexLoc() {
        return vertexLoc;
    }
}
