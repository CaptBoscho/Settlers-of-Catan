package shared.map;

import shared.definitions.PortType;
import shared.locations.VertexLocation;

/**
 * Created by joel on 1/16/16.
 */
public class Port {

    private PortType portType;
    private VertexLocation vertexLoc;

    public Port(PortType portType, VertexLocation vertexLoc) {

    }

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

    public boolean existsBuilding() {
        return false;
    }

}
