package shared.model.game;

import com.google.gson.JsonObject;
import shared.definitions.CatanColor;
import shared.exceptions.PlayerExistsException;
import shared.exceptions.PlayerExistsException;
import shared.model.cards.Card;
import shared.model.cards.resources.ResourceCard;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

import shared.model.bank.InvalidTypeException;
import shared.model.cards.resources.ResourceCard;

import shared.model.map.Map;
import shared.model.player.Player;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.model.player.PlayerManager;

import javax.annotation.Resource;
import javax.naming.InsufficientResourcesException;
import java.util.*;

interface IGame {

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
    void updateGame(final JsonObject json);

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
     * Initiates placing a road on the map
     * @param playerIndex
     * @param edge
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     * @throws StructureException
     */
    void initiateRoad(int playerIndex,  EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException;

    /**
     * Action - Player discards cards
     * @param playerIndex Index of Player performing action
     * @param cards Cards to be discarded
     */
    void discardCards(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class

    /**
     * Action - Player rolls the dice
     * @param playerIndex Index of Player performing action
     */
    int rollNumber(int playerIndex) throws InvalidDiceRollException;

    /**
     * Action - Player offers trade
     * @param playerIndexOne Index of Player offering the trade
     * @param playerIndexTwo Index of Player being offered the trade
     */
    void offerTrade(int playerIndexOne, int playerIndexTwo, List<ResourceType> playerOneCards, List<ResourceType> playerTwoCards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Action - Player plays Year of Plenty
     * @param playerID ID of Player performing action
     */
    void useYearOfPlenty(int playerID, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

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
    PlayerManager getPlayerManager();

    int getId();

    CatanColor getPlayerColorByIndex(int id) throws PlayerExistsException;
    //===================================================================================
    //endregion

    //region Setters
    void setId(int id);
    //endregion

    /**
     * Action - Player plays Road Builder
     * @param playerID ID of Player performing action
     */
    void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException;


    /**
     * Action - Player plays Soldier
     * @param playerID ID of Player performing action
     */
    Set<Integer> useSoldier(int playerID, HexLocation hexloc) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException;

    /**
     * Action - Player plays Monopoly
     * @param playerID ID of Player performing action
     */
    void useMonopoly(int playerID, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;


    /**
     * Action - Player plays Monument
     * @param playerID ID of Player performing action
     */
    void useMonument(int playerID) throws PlayerExistsException, DevCardException;


    /**
     * Action - Player places the Robber
     * @param playerID ID of Player performing action
     */
    Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException;

    void rob(int playerrobber, int playerrobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException;

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
     * builds a road for the player
     */
    void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;



    /**
     * builds a settlement for this player
     */
    void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;


    /**
     * builds a city for this player
     * @param playerID
     */
    void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * Get the player with the longest road card
     * @return
     */
    int getPlayerWithLongestRoad();

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    void setPlayerWithLongestRoad(int playerIDOld, int playerIDNew, int roadSize);

    /**
     * returns the value of how many soldiers is the LargestArmy
     *
     * @return
     */
    int currentLargestArmySize();

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    int getPlayerWithLargestArmy();

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LargestArmy for playerIDNew and armySize
     * @param playerIDOld
     * @param playerIDNew
     * @param armySize
     */
    void newLargestArmy(int playerIDOld, int playerIDNew, int armySize);



    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     * @param playerID
     */
    DevCardType buyDevelopmentCard(int playerID) throws PlayerExistsException, Exception;

    //region Domestic trade methods
    //===================================================================================
    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    boolean canTrade(int playerID);

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


    /**
     * effectuates a trade based on the port type
     * @param playerID
     * @param port
     */
    void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException;


    void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException;


    Map getMap();

    boolean ableToBuildSettlement(int id) throws PlayerExistsException;

    boolean ableToBuildRoad(int id) throws PlayerExistsException;

    boolean ableToBuildCity(int id) throws PlayerExistsException;

    Integer getAvailableRoads(int id) throws PlayerExistsException;

    Integer getAvailableSettlements(int id) throws PlayerExistsException;

    Integer getAvailableCities(int id) throws PlayerExistsException;

    List<Player> getPlayers();

    Integer numberOfDevCard(int id) throws PlayerExistsException;

    Player getWinner() throws GameOverException;

    int getNumberDevCards(DevCardType type, int playerID);

    int getNumberResourceCards(int playerIndex) throws PlayerExistsException;

    int amountOwnedResource(int playerID, ResourceType t)throws PlayerExistsException, InvalidTypeException;

    void buildFirstRoad(int playerID, EdgeLocation hexloc);

    void cancelSoldierCard(int playerID);

    void deleteRoad(int playerID, EdgeLocation edge);

    void cancelRoadBuildingCard(int playerID);

    Player getPlayerById(int id) throws PlayerExistsException;

    int getNumberOfSoldiers(int playerIndex);

    boolean hasDiscarded(int playerIndex);

    MessageList getLog();

    CatanColor getPlayerColorByName(String player);

    HashMap<ResourceType, Integer> getBankResources();

    HashMap<ResourceType, Integer> getPlayerResources(int pIndex) throws PlayerExistsException;

    MessageList getChat();

    int getPoints(int playerIndex) throws PlayerExistsException;

    int getWinnerId();

    String getPlayerNameByIndex(int playerIndex) throws PlayerExistsException;

    int getPlayerIdByIndex(int playerIndex) throws PlayerExistsException;
}