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
public class Port {

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

    public PortType getPortType() {
        return portType;
    }

    public VertexLocation getVertexLoc() {
        return vertexLoc;
    }
}
