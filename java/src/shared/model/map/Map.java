package shared.model.map;

import shared.locations.*;
import shared.definitions.*;
import shared.model.structures.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representation of the map in the game
 */
public class Map {

    private HashMap<HexLocation, Hex> hexMap;
    private HashMap<EdgeLocation, Edge> edgeMap;
    private HashMap<VertexLocation, Vertex> vertexMap;
    private HashMap<Integer, ArrayList<HexLocation>> chitMap;
    private Robber robber;
    private HashMap<EdgeLocation, Road> roads;
    private HashMap<VertexLocation, Building> buildings;
    private HashMap<VertexLocation, ArrayList<Port>> ports;

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
     * Builds a building for initial setup
     * @param vertexLoc VertexLocation
     * @param userID int
     * @param isSecondTurn True if this is the second initiation, false if it is the first
     * @throws StructureException Throws exception if vertex location is next to another building
     */
    public void initiateBuilding(VertexLocation vertexLoc, int userID, boolean isSecondTurn) throws StructureException {

    }

    /**
     * Builds a road for initial setup
     * @param edgeLoc EdgeLocation
     * @param userID int
     * @throws StructureException Throws exception if edge location is not connected to a building
     */
    public void initiateEdge(EdgeLocation edgeLoc, int userID) throws StructureException {

    }

    /**
     * Tells if a Building can be built at a specific vertex location.
     * Checks if neighboring vertices are occupied.
     * Checks if the user has a connecting road.
     * @param vertexLoc VertexLocation
     * @param userID int
     * @return boolean
     */
    public boolean canBuildBuilding(VertexLocation vertexLoc, int userID) {
        return false;
    }

    /**
     * Builds a Building at a vertex location for a specific player.
     * @param vertexLoc VertexLocation
     * @param userID int
     * @throws StructureException Throws exception if the player is not allowed to build a Building
     * at the vertex location.
     */
    public void buildBuilding(VertexLocation vertexLoc, int userID) throws StructureException {

    }

    /**
     * Tells if a Road can be built at a specific edge location
     * checks if the user has a connecting road.
     * @param edgeLoc EdgeLocation
     * @param userID int
     * @return boolean
     */
    public boolean canBuildRoad(EdgeLocation edgeLoc, int userID) {
        return false;
    }

    /**
     * Builds a Road at an edge location for a specific player.
     * @param edgeLoc EdgeLocation
     * @param userID int
     * @throws StructureException Throws exception if the player is not allowed to build a Road at the edge location.
     * @return returns size of longest road if the player has the longest road. Otherwise returns zero.
     */
    public int buildRoad(EdgeLocation edgeLoc, int userID) throws StructureException {
        return 0;
    }

    /**
     * Tells the size of the longest road the player has
     * @param userID
     * @return int Size of longest road
     */
    public int getLongestRoadSize(int userID) {
        return 0;
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

    /**
     * Moves the Robber to a new hex location
     * @param hexLoc HexLocation
     * @throws AlreadyRobbedException Throws exception if Robber is moved to where it is already at
     */
    public void moveRobber(HexLocation hexLoc) throws AlreadyRobbedException {

    }

    /**
     * Tells whether a player has a port at a vertex location
     * @param vertexLoc VertexLocation
     * @param userID int
     * @return
     */
    public boolean hasPort(VertexLocation vertexLoc, int userID) {
        return false;
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
