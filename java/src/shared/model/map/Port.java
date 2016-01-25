package shared.model.map;

import shared.definitions.PortType;
import shared.locations.VertexLocation;

/**
 * Representation of Port in the game. A port is a special structure that players can interact with to trade certain
 * resources for others depending on the implemented port.
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

}
