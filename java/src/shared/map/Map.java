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

    public Hex getHex(HexLocation hexLoc) {
        return null;
    }

    public Edge getEdge(EdgeLocation edgeLoc) {
        return null;
    }

    public Vertex getVertex(VertexLocation vertexLoc) {
        return null;
    }

    public boolean canBuildBuilding(VertexLocation vertexLoc) {
        return false;
    }

    public boolean canBuildRoad(EdgeLocation edgeLoc) {
        return false;
    }

    public PortType getPortType(VertexLocation vertexLoc) {
        return null;
    }

}
