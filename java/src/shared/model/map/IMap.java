package shared.model.map;

import shared.definitions.*;
import shared.exceptions.*;
import shared.locations.*;

import java.util.*;

/**
 * Interface for the Map
 */
public interface IMap {

    /**
     * Gives resources out to players
     * @param diceRoll int In the range of 2 to 12, excluding 7
     * @return A map of resources to give to each player
     * @throws InvalidDiceRollException Throws exception if diceRoll is less than 2 or greater than 12 or equal to 7
     */
    public java.util.Map<Integer, List<ResourceType>> getResources(int diceRoll) throws InvalidDiceRollException;

    /**
     * Informs if a Settlement can be initiated at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public boolean canInitiateSettlement(int playerID, VertexLocation vertexLoc) throws InvalidPlayerException,
            InvalidLocationException;

    /**
     * Builds a Settlement in setup phase and gives out resources if it is the players second turn
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return A list of resources to give to the player
     * @throws StructureException Throws exception if the Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public List<ResourceType> initiateSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a road can be initiated at an edge location if connected to a vertex location
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     * @throws InvalidLocationException Throws exception if Edge/Vertex locaiton is not on the map
     */
    public boolean canInitiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc)
            throws InvalidPlayerException, InvalidLocationException;

    /**
     * Builds a Road in setup phase
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @param vertexLoc VertexLocation where the Settlement is that the Road is connected to
     * @throws StructureException Throws exception if the Road can't be built at the EdgeLocation
     * @throws InvalidLocationException Throws exception if vertex/edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public void initiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a Road can be built at an edge location
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edgeLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a Road at an edge location
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws Exception if Road can't be built at the edge location
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public void buildRoad(int playerID, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException,
            InvalidPlayerException;

    /**
     * Informs if a Settlement can be built at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public boolean canBuildSettlement(int playerID, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a Settlement at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public void buildSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a City can be built at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public boolean canBuildCity(int playerID, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a City at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a City can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public void buildCity(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Gets the size of the longest road of a player
     * @param playerID int
     * @return int Size of the longest road of a player
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public int getLongestRoadSize(int playerID) throws InvalidPlayerException;

    /**
     * Gets all the port types that a player has
     * @param playerID int
     * @return A set of port types
     * @throws InvalidPlayerException Throws exception if playerID is invalid
     */
    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException;

    /**
     * Informs if the robber can be moved to a hex location
     * @param hexLoc HexLocation
     * @return boolean
     * @throws AlreadyRobbedException Throws exception if the hex location is not on the map
     */
    public boolean canMoveRobber(HexLocation hexLoc) throws InvalidLocationException;

    /**
     * Informs who can be robbed at a hex location
     * @return A set of playerID that can be robbed
     */
    public Set<Integer> whoCanGetRobbed();

    /**
     * Moves the Robber to a new hex location
     * @param hexLoc HexLocation
     * @return A set of playerID that can be robbed
     * @throws AlreadyRobbedException Throws exception if Robber is moved to where it is already at
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public Set<Integer> moveRobber(HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException;

}
