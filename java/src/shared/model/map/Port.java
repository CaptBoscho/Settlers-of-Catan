package shared.model.map;

import shared.definitions.PortType;
import shared.locations.VertexLocation;

/**
 * Representation of Port in the game. A port is a special structure that
 * players can interact with to trade certain resources for others depending on
 * the implemented port.
 *
 * @author Joel Bradley
 */
public final class Port {

    private PortType portType;
    private VertexLocation vertexLoc;

    /**
     * Default Constructor
     * @param portType PortType
     * @param vertexLoc VertexLocation
     */
    public Port(final PortType portType, final VertexLocation vertexLoc) {
        assert portType != null;
        assert vertexLoc != null;

        this.portType = portType;
        this.vertexLoc = vertexLoc;
    }

    public PortType getPortType() {
        return portType;
    }
}
