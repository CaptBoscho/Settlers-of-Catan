package shared.model.map;

import com.google.gson.JsonObject;
import shared.definitions.PortType;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;

/**
 * Representation of Port in the game. A port is a special structure that players can interact with to trade certain
 * resources for others depending on the implemented port.
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

    }

    /**
     * Construct a Port object from a JSON blob
     *
     * @param json The JSON representing the object
     */
    public Port(JsonObject json) {

    }

    /**
     * Tells if there is a Building at that Port
     * @return boolean
     */
    public boolean existsBuilding() {
        return false;
    }

    /*
    Getters and Setters
     */
    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    public void setVertexLoc(VertexLocation vertexLoc) {
        this.vertexLoc = vertexLoc;
    }

    public PortType getPortType() {
        return portType;
    }

    public VertexLocation getVertexLoc() {
        return vertexLoc;
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
