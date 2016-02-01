package shared.model.map;

import shared.definitions.PortType;
import shared.exceptions.*;
import shared.locations.*;

import java.util.Set;

/**
 * Interface for the Map
 */
public interface IMap {

    /**
     * Gives resources out to players
     * @param diceRoll int In the range of 2 to 12
     * @throws InvalidDiceRollException Throws exception if diceRoll is less than 2 or greater than 12
     */
    public void giveResources(int diceRoll) throws InvalidDiceRollException;

    /**
     * Builds a Settlement in setup phase and gives out resources if it is the players second turn
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if the Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public void initiateSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException;

    /**
     * Builds a Road in setup phase
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @param vertexLoc VertexLocation where the Settlement is that the Road is connected to
     * @throws StructureException Throws exception if the Road can't be built at the EdgeLocation
     * @throws InvalidLocationException Throws exception if vertex/edge location is not on the map
     */
    public void initiateRoad(int playerID, EdgeLocation edgeLoc, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException;

    /**
     * Informs if a Road can be built at an edge location
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edgeLoc) throws InvalidLocationException;

    /**
     * Builds a Road at an edge location
     * @param playerID int
     * @param edgeLoc EdgeLocation
     * @throws StructureException Throws Exception if Road can't be built at the edge location
     * @throws InvalidLocationException Throws exception if edge location is not on the map
     */
    public void buildRoad(int playerID, EdgeLocation edgeLoc) throws StructureException, InvalidLocationException;

    /**
     * Informs if a Settlement can be built at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public boolean canBuildSettlement(int playerID, VertexLocation vertexLoc) throws InvalidLocationException;

    /**
     * Builds a Settlement at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a Settlement can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public void buildSettlement(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException;

    /**
     * Informs if a City can be built at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @return boolean
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public boolean canBuildCity(int playerID, VertexLocation vertexLoc) throws InvalidLocationException;

    /**
     * Builds a City at a vertex location
     * @param playerID int
     * @param vertexLoc VertexLocation
     * @throws StructureException Throws exception if a City can't be built at the vertex location
     * @throws InvalidLocationException Throws exception if vertex location is not on the map
     */
    public void buildCity(int playerID, VertexLocation vertexLoc) throws StructureException,
            InvalidLocationException;

    /**
     * Gets the size of the longest road of a player
     * @param playerID int
     * @return int Size of the longest road of a player
     */
    public int getLongestRoadSize(int playerID);

    /**
     * Gets all the port types that a player has
     * @param playerID int
     * @return Set</PortType>
     */
    public Set<PortType> getPortTypes(int playerID);

    /**
     * Moves the Robber to a new hex location
     * @param hexLoc HexLocation
     * @throws AlreadyRobbedException Throws exception if Robber is moved to where it is already at
     */
    public void moveRobber(HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException;

}
