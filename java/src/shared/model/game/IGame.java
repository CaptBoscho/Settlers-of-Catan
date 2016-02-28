package shared.model.game;

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

public interface IGame {

    /**
     * Starts the game, returns the Id for the first player
     * @param players
     * @return
     */
    public int initializeGame(List<Player> players, boolean randomhex, boolean randomchits, boolean randomports);

    /**
     * returns the playerID for whose turn it is
     * @return
     */
    public int getCurrentTurn();


    public boolean canInitiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException;


    public boolean canInitiateRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException;

    public void initiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException;

    public void initiateRoad(int playerID,  EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException;

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     *        and hexes from dice roll
     * @param playerID ID of Player performing action
     * @return True if Player can discard cards
     */
    boolean canDiscardCards(int playerID) throws PlayerExistsException;

    /**
     * Action - Player discards cards
     * @param playerID ID of Player performing action
     * @param cards Cards to be discarded
     */
    void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException; // TODO: 1/30/2016 Would be better with Card generic class


    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     * @param playerID ID of Player performing action
     * @return True if Player can roll the die
     */
    boolean canRollNumber(int playerID);

    /**
     * Action - Player rolls the dice
     * @param playerID ID of Player performing action
     */
    int rollNumber(int playerID) throws InvalidDiceRollException;

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     * @param playerID ID of Player performing action
     * @return True if Player can offer a trade
     */
    boolean canOfferTrade(int playerID);

    /**
     * Action - Player offers trade
     * @param playerIDOne ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException;


    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     * @param playerID ID of Player performing action
     * @return True if Player can finish their turn
     */
    boolean canFinishTurn(int playerID);

    /**
     * Action - Player finishes their turn
     * @param playerID ID of Player performing action
     */
    Integer finishTurn(int playerID) throws Exception;

    public TurnTracker.Phase getCurrentPhase();

    void nextPhase();


    void addObserver(Observer o);

    PlayerManager getPlayerManager();

    int getId();

    CatanColor getPlayerColorByIndex(int id) throws PlayerExistsException;

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Year of Plenty
     */
    boolean canUseYearOfPlenty(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Year of Plenty
     * @param playerID ID of Player performing action
     */
    void useYearOfPlenty(int playerID, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Road Builder
     */
    boolean canUseRoadBuilder(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Road Builder
     * @param playerID ID of Player performing action
     */
    void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException;

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Soldier
     */
    boolean canUseSoldier(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Soldier
     * @param playerID ID of Player performing action
     */
    Set<Integer> useSoldier(int playerID, HexLocation hexloc) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException;

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Monopoly
     */
    boolean canUseMonopoly(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Monopoly
     * @param playerID ID of Player performing action
     */
    void useMonopoly(int playerID, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException;

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     * @param playerID ID of Player performing action
     * @return True if Player can play Monument
     */
    boolean canUseMonument(int playerID) throws PlayerExistsException;

    /**
     * Action - Player plays Monument
     * @param playerID ID of Player performing action
     */
    void useMonument(int playerID) throws PlayerExistsException, DevCardException;

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     * @param playerID ID of Player performing action
     * @return True if Player can place the Robber
     */
    boolean canPlaceRobber(int playerID, HexLocation hexloc);

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
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     * @param playerID
     * @return
     */
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a road for the player
     */
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * checks if the player has the cards to build a settlement
     * @return
     */
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a settlement for this player
     */
    public void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * checks if the player has the cards to ubild a city
     * @return
     */
    public boolean canBuildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException;

    /**
     * builds a city for this player
     * @param playerID
     */
    public void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException;

    /**
     * returns the value of how many roads is the LongestRoad
     * @return
     */
    public int currentLongestRoadSize();

    /**
     * returns the playerID of who owns the current longest road
     * @return
     */
    public int currentLongestRoadPlayer();

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    public void newLongestRoad(int playerIDOld, int playerIDNew, int roadSize);

    /**
     * returns the value of how many soldiers is the LargestArmy
     *
     * @return
     */
    public int currentLargestArmySize();

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    public int currentLargestArmyPlayer();

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LargestArmy for playerIDNew and armySize
     * @param playerIDOld
     * @param playerIDNew
     * @param armySize
     */
    public void newLargestArmy(int playerIDOld, int playerIDNew, int armySize);

    /**
     * checks if the player has the cards to buy a DevelopmentCard
     * @param playerID
     * @return
     */
    public boolean canBuyDevelopmentCard(int playerID) throws PlayerExistsException;

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     * @param playerID
     */
    public DevCardType buyDevelopmentCard(int playerID) throws PlayerExistsException, Exception;

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    public boolean canTrade(int playerID);

    public boolean isTradeActive();

    public int getTradeReceiver();

    public int getTradeSender();

    public int getTradeBrick();

    public int getTradeWood();

    public int getTradeSheep();

    public int getTradeWheat();

    public int getTradeOre();

    /**
     * checks if that player has the card needed for that port's trade
     * @param playerID
     * @param port
     * @return
     */
    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException;

    /**
     * effectuates a trade based on the port type
     * @param playerID
     * @param port
     */
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException;


    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException;

    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException;


    public Map getMap();

    public boolean ableToBuildSettlement(int id) throws PlayerExistsException;

    public boolean ableToBuildRoad(int id) throws PlayerExistsException;

    public boolean ableToBuildCity(int id) throws PlayerExistsException;

    public Integer getAvailableRoads(int id) throws PlayerExistsException;

    public Integer getAvailableSettlements(int id) throws PlayerExistsException;

    public Integer getAvailableCities(int id) throws PlayerExistsException;

    public List<Player> getPlayers();

    public Integer numberOfDevCard(int id) throws PlayerExistsException;

    public Player getWinner() throws GameOverException;

    int getNumberDevCards(DevCardType type, int playerID);

    int getNumberResourceCards(int playerIndex) throws PlayerExistsException;

    public int amountOwnedResource(int playerID, ResourceType t)throws PlayerExistsException, InvalidTypeException;

    public void buildFirstRoad(int playerID, EdgeLocation hexloc);

    public void cancelSoldierCard(int playerID);

    public void deleteRoad(int playerID, EdgeLocation edge);

    public void cancelRoadBuildingCard(int playerID);

    public Player getPlayerById(int id) throws PlayerExistsException;

    int getNumberOfSoldiers(int playerIndex);

    boolean hasDiscarded(int playerIndex);

    MessageList getLog();

    CatanColor getPlayerColorByName(String player);

    public HashMap<ResourceType, Integer> getBankResources();

    MessageList getChat();

    int getPoints(int playerIndex) throws PlayerExistsException;

    int getWinnerIndex();

    String getPlayerNameByIndex(int playerIndex) throws PlayerExistsException;
}