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
    java.util.Map<Integer, List<ResourceType>> getResources(int diceRoll) throws InvalidDiceRollException;

    /**
     * Informs if a Settlement can be initiated at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    boolean canInitiateSettlement(int playerIndex, VertexLocation vertexLoc) throws InvalidPlayerException,
            InvalidLocationException;

    /**
     * Builds a Settlement in setup phase and gives out resources if it is the players second turn
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return A list of resources to give to the player
     * @throws StructureException Throws exception if the Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    List<ResourceType> initiateSettlement(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a road can be initiated at an edge location if connected to a vertex location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @return boolean
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     * @throws InvalidLocationException Throws exception if Edge/Vertex locaiton is not on the map
     */
    boolean canInitiateRoad(int playerIndex, EdgeLocation edgeLoc)
            throws InvalidPlayerException, InvalidLocationException;

    /**
     * Builds a Road in setup phase
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws exception if the Road can't be built at the EdgeLocation
     * @throws InvalidLocationException Throws exception if vertex/edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    void initiateRoad(int playerIndex, EdgeLocation edgeLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a Road can be built at an edge location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    boolean canBuildRoad(int playerIndex, EdgeLocation edgeLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a Road at an edge location
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws Exception if Road can't be built at the edge location
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    void buildRoad(int playerIndex, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException,
            InvalidPlayerException;

    /**
     * Informs if a Settlement can be built at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    boolean canBuildSettlement(int playerIndex, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a Settlement at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    void buildSettlement(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Informs if a City can be built at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    boolean canBuildCity(int playerIndex, VertexLocation vertexLoc) throws InvalidLocationException,
            InvalidPlayerException;

    /**
     * Builds a City at a vertex location
     * @param playerIndex int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a City can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    void buildCity(int playerIndex, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException, InvalidPlayerException;

    /**
     * Gets the size of the longest road of a player
     * @param playerIndex int
     * @return int Size of the longest road of a player
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    int getLongestRoadSize(int playerIndex) throws InvalidPlayerException;

    /**
     * Gets all the port types that a player has
     * @param playerIndex int
     * @return A set of port types
     * @throws InvalidPlayerException Throws exception if playerIndex is invalid
     */
    Set<PortType> getPortTypes(int playerIndex) throws InvalidPlayerException;

    /**
     * Informs if the robber can be moved to a hex location
     * @param hexLoc HexLocation
     * @return boolean
     * @throws InvalidLocationException
     */
    boolean canMoveRobber(HexLocation hexLoc) throws InvalidLocationException;

    /**
     * Informs who can be robbed at a hex location
     * @return A set of playerIndex that can be robbed
     */
    Set<Integer> whoCanGetRobbed(int playerIndex);

    /**
     * Moves the Robber to a new hex location
     * @param hexLoc HexLocation
     * @return A set of playerIndex that can be robbed
     * @throws AlreadyRobbedException Throws exception if Robber is moved to where it is already at
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    Set<Integer> moveRobber(int playerIndex, HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException;

    /**
     * Deletes a road if a player cancels in the middle of the play road building card
     * @param playerIndex int
     * @param edgeLoc EdgeLocation
     * @throws InvalidLocationException throws exception if edge locaiton is not on the map
     * @throws StructureException throws exception if edge location doesn't have a road to delete
     */
    void deleteRoad(int playerIndex, EdgeLocation edgeLoc) throws InvalidLocationException, StructureException;

}
