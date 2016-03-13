package shared.model.game;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.player.PlayerManager;
import javax.naming.InsufficientResourcesException;
import java.util.*;

public interface IGame {
    //region Game methods
    //================================================================================
    /**
     * Starts the game, returns the Id for the first player
     * @param players
     * @return
     */
    int initializeGame(List<Player> players, boolean randomHex, boolean randomChits, boolean randomPorts);

    /**
     * Updates the game
     * @param json
     */
    void updateGame(JsonObject json);

    /**
     * Gets the current version of the game model
     * @return
     */
    int getVersion();

    /**
     * Gets the playerIndex of the player who's turn it is
     * @return
     */
    int getCurrentTurn();

    /**
     * Get all of the players in the game
     * @return
     */
    List<Player> getPlayers();

    /**
     * Build the first road as part of the road building card
     * @param playerIndex
     * @param location
     */
    void buildFirstRoad(int playerIndex, EdgeLocation location);

    /**
     * Remove a road from the map
     * @param playerIndex
     * @param edge
     */
    void deleteRoad(int playerIndex, EdgeLocation edge);

    /**
     * Gets the phase of the current turn
     * @return
     */
    TurnTracker.Phase getCurrentPhase();

    /**
     * Moves turn to the next phase
     */
    void nextPhase();
    //=================================================================================
    //endregion

    //region Can do methods
    //=================================================================================
    /**
     * Determine if a settlement can be built by the player at the location
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     */
    boolean canInitiateSettlement(int playerIndex, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException;

    /**
     * Determine if the player can build a settlement (Check their bank)
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     */
    boolean canBuildSettlement(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * Determine if the player can build a city (Check their bank)
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     */
    boolean canBuildCity(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * Determine if road can be built by the player at the location
     * @param playerIndex
     * @param edge
     * @return
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     */
    boolean canInitiateRoad(int playerIndex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException;

    /**
     * Determine if Player can build a road at the location
     * @param playerIndex
     * @return
     */
    boolean canBuildRoad(int playerIndex, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @param playerIndex Index of Player performing action
     * @return True if Player can discard cards
     */
    boolean canDiscardCards(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     * @param playerIndex Index of Player performing action
     * @return True if Player can roll the die
     */
    boolean canRollNumber(int playerIndex);

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @param playerIndex Index of Player performing action
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade(int playerIndex);

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilding(int playerIndex) throws PlayerExistsException;

    /**
     * Checks to see if a road can be built on the map for the road building card
     * @param playerID int
     * @param edge EdgeLocation
     * @return boolean
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws PlayerExistsException
     */
    boolean canPlaceRoadBuildingCard(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Monument
     */
    boolean canUseMonument(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @param playerIndex Index of Player performing action
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber(int playerIndex, HexLocation hexLocation);

    /**
     * Determine if the player can buy a development card
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    boolean canBuyDevelopmentCard(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if the player can perform the specified maritime trade
     * @param playerIndex
     * @param port
     * @return
     * @throws InvalidPlayerException
     * @throws PlayerExistsException
     */
    boolean canMaritimeTrade(int playerIndex, PortType port) throws InvalidPlayerException, PlayerExistsException;

    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     * @param playerIndex Index of Player performing action
     * @return True if Player can finish their turn
     */
    boolean canFinishTurn(int playerIndex);
    //End of can do methods
    //=========================================================================
    //endregion

    //region Do methods
    //==========================================================================
    /**
     * Initiates placing a settlement on the map
     * @param playerIndex
     * @param vertex
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     * @throws StructureException
     */
    void initiateSettlement(int playerIndex, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException;

    /**
     * Action - Player builds a settlement
     * @param playerIndex
     * @param vertex
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    void buildSettlement(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * Initiates placing a road on the map
     * @param playerIndex
     * @param edge
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     * @throws StructureException
     */
    void initiateRoad(int playerIndex,  EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException;

    /**
     * Action - Player builds a road
     * @param playerIndex
     * @param edge
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    void buildRoad(int playerIndex, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * Action - Player builds a city
     * @param playerIndex
     * @param vertex
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    void buildCity(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * Action - Player discards cards
     * @param playerIndex Index of Player performing action
     * @param cards Cards to be discarded
     */
    void discardCards(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class

    /**
     * Action - Player offers trade
     * @param playerIndexOne Index of Player offering the trade
     * @param playerIndexTwo Index of Player being offered the trade
     */
    void offerTrade(int playerIndexOne, int playerIndexTwo, List<ResourceType> playerOneCards, List<ResourceType> playerTwoCards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Action - Player plays Year of Plenty
     * @param playerIndex ID of Player performing action
     */
    void useYearOfPlenty(int playerIndex, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Action - Player plays Road Builder
     * @param playerIndex ID of Player performing action
     */
    void useRoadBuilder(int playerIndex, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException;

    /**
     * Cancels playing the road building card
     * @param playerIndex
     */
    void cancelRoadBuildingCard(int playerIndex);

    /**
     * Action - Player plays Soldier
     * @param playerIndex ID of Player performing action
     * @param location
     */
    Set<Integer> useSoldier(int playerIndex, HexLocation location) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException;

    /**
     * Cancels playing a soldier card
     * @param playerIndex
     */
    void cancelSoldierCard(int playerIndex);

    /**
     * Action - Player plays Monopoly
     * @param playerIndex ID of Player performing action
     */
    void useMonopoly(int playerIndex, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;


    /**
     * Action - Player plays Monument
     * @param playerIndex Index of Player performing action
     */
    void useMonument(int playerIndex) throws PlayerExistsException, DevCardException;


    /**
     * Action - Player places the Robber
     * @param playerIndex Index of Player performing action
     */
    Set<Integer> placeRobber(int playerIndex, HexLocation location) throws AlreadyRobbedException, InvalidLocationException;

    /**
     * Action - Player robs another player
     * @param playerRobber
     * @param playerRobbed
     * @throws MoveRobberException
     * @throws InvalidTypeException
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     */
    void rob(int playerRobber, int playerRobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException;

    /**
     * Action - Player buys a new developmentCard
     * deducts cards
     * adds new developmentCard to his DCBank
     * @param playerIndex
     */
    DevCardType buyDevelopmentCard(int playerIndex) throws PlayerExistsException, Exception;

    /**
     * Action - Player performs a maritime trade
     * @param playerIndex
     * @param port
     */
    void maritimeTrade(int playerIndex, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException;

    /**
     * Action - Player finishes their turn
     * @param playerIndex Index of Player performing action
     */
    Integer finishTurn(int playerIndex) throws Exception;
    //===================================================================================
    //endregion

    //region Utility methods
    //Utility methods
    //===================================================================================
    void addObserver(Observer o);
    //===================================================================================
    //endregion

    //region Getters
    //===================================================================================
    /**
     * Gets the instance of the player manager
     * @return
     */
    PlayerManager getPlayerManager();

    /**
     * Gets the instance of the turn tracker
     * @return
     */
    TurnTracker getTurnTracker();

    /**
     * Gets the map instance
     * @return
     */
    Map getMap();

    /**
     * Gets the chat
     * @return
     */
    MessageList getChat();

    /**
     * Gets the game's id
     * @return
     */
    int getId();

    /**
     * Get the player with the longest road card
     * @return
     */
    int getPlayerWithLongestRoad();

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    int getPlayerWithLargestArmy();

    /**
     * Get the type of ports owned by the player
     * @param playerIndex
     * @return
     * @throws InvalidPlayerException
     */
    Set<PortType> getPortTypes(int playerIndex) throws InvalidPlayerException;

    /**
     * Get the number of roads the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    Integer getAvailableRoads(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of settlements the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    Integer getAvailableSettlements(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of cities the player has left
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    Integer getAvailableCities(int playerIndex) throws PlayerExistsException;

    /**
     * Get a player by its id
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    Player getPlayerById(int id) throws PlayerExistsException;

    /**
     * Get the number of soldiers the specified player has
     * @param playerIndex
     * @return
     */
    int getNumberOfSoldiers(int playerIndex);

    /**
     * Check if the player has discarded this phase
     * @param playerIndex
     * @return
     */
    boolean hasDiscarded(int playerIndex);

    /**
     * Get the game log - list of events that have occured in the game
     * @return
     */
    MessageList getLog();

    /**
     * Get the resources left in the bank
     * @return
     */
    HashMap<ResourceType, Integer> getBankResources();

    /**
     * Get the player's resources
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    HashMap<ResourceType, Integer> getPlayerResources(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of victory points the player has
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getPoints(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of development cards the player has
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    Integer numberOfDevCard(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of development cards (specific type) the player has
     * @param type
     * @param playerID
     * @return
     */
    int getNumberDevCards(DevCardType type, int playerID);

    /**
     * Get the number of resource cards the player has
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    int getNumberResourceCards(int playerIndex) throws PlayerExistsException;

    /**
     * Get the number of resources cards (specific type) the player has
     * @param playerIndex
     * @param t
     * @return
     * @throws PlayerExistsException
     * @throws InvalidTypeException
     */
    int amountOwnedResource(int playerIndex, ResourceType t)throws PlayerExistsException, InvalidTypeException;

    /**
     * Get the game winner
     * @return
     * @throws GameOverException
     */
    Player getWinner() throws GameOverException;

    /**
     * Get player id associated with the given index
     * @param playerIndex
     * @return
     */
    int getPlayerIdByIndex(int playerIndex) throws PlayerExistsException; // TODO: 3/12/2016 This really should be removed :(

    /**
     * Get the player's name
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    String getPlayerNameByIndex(int playerIndex) throws PlayerExistsException;

    /**
     * Get the player's color based on their name
     * @param player
     * @return
     */
    CatanColor getPlayerColorByName(String player);

    /**
     * Get the color of the specified player
     * @param playerIndex index of the player
     * @return
     * @throws PlayerExistsException
     */
    CatanColor getPlayerColorByIndex(int playerIndex) throws PlayerExistsException;
    //===================================================================================
    //endregion

    //region Setters
    //===================================================================================
    /**
     * Sets the game's id
     * @param id
     */
    void setId(int id);

    /**
     * Sets the game's player manager
     * @param playerManager
     */
    void setPlayerManager(PlayerManager playerManager);

    /**
     * Sets the game's current phase
     * @param phase
     */
    void setPhase(TurnTracker.Phase phase);
    //====================================================================================
    //endregion

    //region Domestic trade methods
    //===================================================================================
    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerIndex
     * @return
     */
    boolean canTrade(int playerIndex);

    boolean isTradeActive();

    int getTradeReceiver();

    int getTradeSender();

    int getTradeBrick();

    int getTradeWood();

    int getTradeSheep();

    int getTradeWheat();

    int getTradeOre();
    //======================================================================================
    //endregion

    //region ResourceBar controller methods
    //======================================================================
    /**
     * Determine if the player can buy a settlement
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    boolean ableToBuildSettlement(int playerIndex) throws PlayerExistsException;

    /**
     * Determin if the player can buy a road
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    boolean ableToBuildRoad(int playerIndex) throws PlayerExistsException;

    /**
     * Determine if the player can buy a city
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    boolean ableToBuildCity(int playerIndex) throws PlayerExistsException;
    //=======================================================================
    //endregion
}