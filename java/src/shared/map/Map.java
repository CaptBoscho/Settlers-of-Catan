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
     * @param userID
     */
    public void giveResources(int diceNum, int userID) {

    }

    /**
     * Tells if a Building can be built at a specific vertex location.
     * Checks if neighboring vertices are occupied.
     * Checks if the user has a connecting road.
     * @param vertexLoc VertexLocation
     * @param int userID
     * @return boolean
     */
    public boolean canBuildBuilding(VertexLocation vertexLoc, int userID) {
        return false;
    }

    /**
     * Tells if a Road can be built at a specific edge location
     * checks if the user has a connecting road.
     * @param edgeLoc EdgeLocation
     * @param int userID
     * @return boolean
     */
    public boolean canBuildRoad(EdgeLocation edgeLoc, int userID) {
        return false;
    }

    /**
     * Checks if the user has a road connecting to the edge.
     * @param edgeLoc
     * @param userID
     * @return
     */
    public boolean hasConnectingRoadtoEdge(EdgeLocation edgeLoc, int userID){
        return true;
    }

    /**
     * checks if user has a road connecting to the vertex.
     * @param vertexLoc
     * @param userID
     * @return
     */
    public boolean hasConnectingRoadtoVertex(VertexLocation vertexLoc, int userID) {return true;}

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
