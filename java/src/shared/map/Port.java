package shared.map;

import shared.definitions.PortType;
import shared.locations.VertexLocation;

/**
 * Created by joel on 1/16/16.
 */
public class Port {

    private PortType portType;
    private VertexLocation vertexLocation;

    public Port(PortType portType, VertexLocation vertexLocation) {

    }

    public boolean existsBuilding() {
        return false;
    }

}
