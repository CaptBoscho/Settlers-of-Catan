package shared.map;

import shared.locations.*;
import shared.definitions.*;
import java.util.HashMap;

/**
 * Representation of the map in the game
 */
public class Map {

    private HashMap<HexLocation, Hex> hexMap;
    private HashMap<EdgeLocation, Edge> edgeMap;
    private HashMap<VertexLocation, Vertex> vertexMap;
    private HashMap<Integer, HexLocation> chitMap;

    /**
     * Default Constructor that initializes the map
     */
    public Map() {

    }

    /**
     * Tells any Building objects on the map to give resources to its player if the chit it needs is rolled
     * @param diceNum Number rolled by dice
     */
    public void giveResources(int diceNum) {

    }

    /**
     * Tells if a Building can be built at a specific vertex location
     * @param vertexLoc VertexLocation
     * @return boolean
     */
    public boolean canBuildBuilding(VertexLocation vertexLoc) {
        return false;
    }

    /**
     * Tells if a Road can be built at a specific edge location
     * @param edgeLoc EdgeLocation
     * @return boolean
     */
    public boolean canBuildRoad(EdgeLocation edgeLoc) {
        return false;
    }

    /**
     * Returns the type of port found at a vertex location
     * @param vertexLoc VertexLocation
     * @return PortType
     */
    public PortType getPortType(VertexLocation vertexLoc) {
        return null;
    }

    /*
    Getters and Setters
     */
    public Hex getHex(HexLocation hexLoc) {
        return null;
    }

    public Edge getEdge(EdgeLocation edgeLoc) {
        return null;
    }

    public Vertex getVertex(VertexLocation vertexLoc) {
        return null;
    }

}
