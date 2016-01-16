package shared.map;

import shared.locations.*;
import shared.definitions.*;
import java.util.HashMap;

/**
 * Created by joel on 1/16/16.
 */
public class Map {

    private HashMap<HexLocation, Hex> hexMap;
    private HashMap<EdgeLocation, Edge> edgeMap;
    private HashMap<VertexLocation, Vertex> vertexMap;

    public Map() {

    }

    public Hex getHex(HexLocation hexLocation) {
        return null;
    }

    public Edge getEdge(EdgeLocation edgeLocation) {
        return null;
    }

    public Vertex getVertex(VertexLocation vertexLocation) {
        return null;
    }

    public boolean canBuildBuilding(VertexLocation vertexLocation) {
        return false;
    }

    public boolean canBuildRoad(EdgeLocation edgeLocation) {
        return false;
    }

    public PortType getPortType(VertexLocation vertexLocation) {
        return null;
    }

}
