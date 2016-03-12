package shared.model.game;

import com.google.gson.JsonObject;
import shared.definitions.*;
import shared.exceptions.PlayerExistsException;
import shared.model.JsonSerializable;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.ResourceCardBank;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;

import shared.definitions.DevCardType;
import shared.model.cards.devcards.DevelopmentCard;

import shared.model.cards.devcards.RoadBuildCard;
import shared.model.cards.devcards.SoldierCard;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;
import shared.model.cards.resources.ResourceCard;


import javax.naming.InsufficientResourcesException;
import java.util.*;
import java.util.Observable;

/**
 * game class representing a Catan game
 */

public class Game extends Observable implements IGame, JsonSerializable {
    //region Member variables
    private int gameId;
    private Map map;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private Trade currentOffer;
    private MessageList chat;
    private MessageList log;
    private int winner;
    private int version;
    //endregion

    //region Constructor
    /**
     * Constructor
     */
    public Game() {
        this.version = -1;
        this.map = new Map(false, false, false);
        this.turnTracker = new TurnTracker();
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager(new ArrayList<>());
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);
        this.chat = new MessageList();
        this.log = new MessageList();
    }
    //endregion

    //region Game Methods
    //===================================================================================
    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @param randomHex
     * @param randomChits
     * @param randomPorts @return
     */
    @Override
    public int initializeGame(List<Player> players, boolean randomHex, boolean randomChits, boolean randomPorts) {
        return 0;
    }

    /**
     * Updates the game
     *
     * @param json
     */
    @Override
    public void updateGame(JsonObject json) {

    }

    /**
     * Gets the current version of the game model
     *
     * @return
     */
    @Override
    public int getVersion() {
        return 0;
    }

    /**
     * Gets the playerIndex of the player who's turn it is
     *
     * @return
     */
    @Override
    public int getCurrentTurn() {
        return 0;
    }

    /**
     * Get all of the players in the game
     *
     * @return
     */
    @Override
    public List<Player> getPlayers() {
        return null;
    }

    /**
     * Build the first road as part of the road building card
     *
     * @param playerIndex
     * @param location
     */
    @Override
    public void buildFirstRoad(int playerIndex, EdgeLocation location) {

    }

    /**
     * Remove a road from the map
     *
     * @param playerIndex
     * @param edge
     */
    @Override
    public void deleteRoad(int playerIndex, EdgeLocation edge) {

    }

    /**
     * Gets the phase of the current turn
     *
     * @return
     */
    @Override
    public TurnTracker.Phase getCurrentPhase() {
        return null;
    }

    /**
     * Moves turn to the next phase
     */
    @Override
    public void nextPhase() {

    }
    //===============================================================================
    //endregion

    //region Can do methods
    //============================================================================================
    /**
     * Determine if a settlement can be built by the player at the location
     *
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     */
    @Override
    public boolean canInitiateSettlement(int playerIndex, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException {
        return false;
    }

    /**
     * Determine if the player can build a settlement (Check their bank)
     *
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     */
    @Override
    public boolean canBuildSettlement(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        return false;
    }

    /**
     * Determine if the player can build a city (Check their bank)
     *
     * @param playerIndex
     * @param vertex
     * @return
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     */
    @Override
    public boolean canBuildCity(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        return false;
    }

    /**
     * Determine if road can be built by the player at the location
     *
     * @param playerIndex
     * @param edge
     * @return
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     */
    @Override
    public boolean canInitiateRoad(int playerIndex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException {
        return false;
    }

    /**
     * Determine if Player can build a road at the location
     *
     * @param playerIndex
     * @param edge
     * @return
     */
    @Override
    public boolean canBuildRoad(int playerIndex, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can roll the die
     */
    @Override
    public boolean canRollNumber(int playerIndex) {
        return false;
    }

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int playerIndex) {
        return false;
    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilding(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Checks to see if a road can be built on the map for the road building card
     *
     * @param playerID int
     * @param edge     EdgeLocation
     * @return boolean
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws PlayerExistsException
     */
    @Override
    public boolean canPlaceRoadBuildingCard(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerIndex Index of Player performing action
     * @param hexLocation
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int playerIndex, HexLocation hexLocation) {
        return false;
    }

    /**
     * Determine if the player can buy a development card
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean canBuyDevelopmentCard(int playerIndex) throws PlayerExistsException {
        return false;
    }

    /**
     * Determine if the player can perform the specified maritime trade
     *
     * @param playerIndex
     * @param port
     * @return
     * @throws InvalidPlayerException
     * @throws PlayerExistsException
     */
    @Override
    public boolean canMaritimeTrade(int playerIndex, PortType port) throws InvalidPlayerException, PlayerExistsException {
        return false;
    }

    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can finish their turn
     */
    @Override
    public boolean canFinishTurn(int playerIndex) {
        return false;
    }
    //==================================================================================================
    //endregion

    //region Do methods
    //=========================================================================================
    /**
     * Initiates placing a settlement on the map
     *
     * @param playerIndex
     * @param vertex
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     * @throws StructureException
     */
    @Override
    public void initiateSettlement(int playerIndex, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException {

    }

    /**
     * Action - Player builds a settlement
     *
     * @param playerIndex
     * @param vertex
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    @Override
    public void buildSettlement(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

    }

    /**
     * Initiates placing a road on the map
     *
     * @param playerIndex
     * @param edge
     * @throws InvalidLocationException
     * @throws InvalidPlayerException
     * @throws StructureException
     */
    @Override
    public void initiateRoad(int playerIndex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException {

    }

    /**
     * Action - Player builds a road
     *
     * @param playerIndex
     * @param edge
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    @Override
    public void buildRoad(int playerIndex, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

    }

    /**
     * Action - Player builds a city
     *
     * @param playerIndex
     * @param vertex
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    @Override
    public void buildCity(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

    }

    /**
     * Action - Player discards cards
     *
     * @param playerIndex Index of Player performing action
     * @param cards       Cards to be discarded
     */
    @Override
    public void discardCards(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {

    }

    /**
     * Action - Player rolls the dice
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public int rollNumber(int playerIndex) throws InvalidDiceRollException {
        return 0;
    }

    /**
     * Action - Player offers trade
     *
     * @param playerIndexOne Index of Player offering the trade
     * @param playerIndexTwo Index of Player being offered the trade
     * @param playerOneCards
     * @param playerTwoCards
     */
    @Override
    public void offerTrade(int playerIndexOne, int playerIndexTwo, List<ResourceType> playerOneCards, List<ResourceType> playerTwoCards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {

    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerID ID of Player performing action
     * @param want1
     * @param want2
     */
    @Override
    public void useYearOfPlenty(int playerID, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {

    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     * @param edge1
     * @param edge2
     */
    @Override
    public void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException {

    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     * @param hexloc
     */
    @Override
    public Set<Integer> useSoldier(int playerID, HexLocation hexloc) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException {
        return null;
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     * @param type
     */
    @Override
    public void useMonopoly(int playerID, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {

    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(int playerID) throws PlayerExistsException, DevCardException {

    }

    /**
     * Action - Player places the Robber
     *
     * @param playerID ID of Player performing action
     * @param hexloc
     */
    @Override
    public Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException {
        return null;
    }

    /**
     * Action - Player robs another player
     *
     * @param playerRobber
     * @param playerRobbed
     * @throws MoveRobberException
     * @throws InvalidTypeException
     * @throws PlayerExistsException
     * @throws InsufficientResourcesException
     */
    @Override
    public void rob(int playerRobber, int playerRobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException {

    }

    /**
     * Action - Player buys a new developmentCard
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerIndex
     */
    @Override
    public DevCardType buyDevelopmentCard(int playerIndex) throws PlayerExistsException, Exception {
        return null;
    }

    /**
     * Action - Player performs a maritime trade
     *
     * @param playerIndex
     * @param port
     * @param want
     */
    @Override
    public void maritimeTrade(int playerIndex, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {

    }

    /**
     * Action - Player finishes their turn
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public Integer finishTurn(int playerIndex) throws Exception {
        return null;
    }
    //================================================================================================
    //endregion

    //region Getters
    //================================================================================
    /**
     * Gets the instance of the player manager
     *
     * @return
     */
    @Override
    public PlayerManager getPlayerManager() {
        return null;
    }

    /**
     * Gets the map instance
     *
     * @return
     */
    @Override
    public Map getMap() {
        return null;
    }

    /**
     * Gets the chat
     *
     * @return
     */
    @Override
    public MessageList getChat() {
        return null;
    }

    /**
     * Gets the game's id
     *
     * @return
     */
    @Override
    public int getId() {
        return 0;
    }

    /**
     * Get the player with the longest road card
     *
     * @return
     */
    @Override
    public int getPlayerWithLongestRoad() {
        return 0;
    }

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    @Override
    public int getPlayerWithLargestArmy() {
        return 0;
    }

    /**
     * Get the type of ports owned by the player
     *
     * @param playerIndex
     * @return
     * @throws InvalidPlayerException
     */
    @Override
    public Set<PortType> getPortTypes(int playerIndex) throws InvalidPlayerException {
        return null;
    }

    /**
     * Get the number of roads the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableRoads(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the number of settlements the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableSettlements(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the number of cities the player has left
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableCities(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get a player by its id
     *
     * @param id
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Player getPlayerById(int id) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the number of soldiers the specified player has
     *
     * @param playerIndex
     * @return
     */
    @Override
    public int getNumberOfSoldiers(int playerIndex) {
        return 0;
    }

    /**
     * Check if the player has discarded this phase
     *
     * @param playerIndex
     * @return
     */
    @Override
    public boolean hasDiscarded(int playerIndex) {
        return false;
    }

    /**
     * Get the game log - list of events that have occured in the game
     *
     * @return
     */
    @Override
    public MessageList getLog() {
        return null;
    }

    /**
     * Get the resources left in the bank
     *
     * @return
     */
    @Override
    public HashMap<ResourceType, Integer> getBankResources() {
        return null;
    }

    /**
     * Get the player's resources
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public HashMap<ResourceType, Integer> getPlayerResources(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the number of victory points the player has
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public int getPoints(int playerIndex) throws PlayerExistsException {
        return 0;
    }

    /**
     * Get the number of development cards the player has
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public Integer numberOfDevCard(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the number of development cards (specific type) the player has
     *
     * @param type
     * @param playerID
     * @return
     */
    @Override
    public int getNumberDevCards(DevCardType type, int playerID) {
        return 0;
    }

    /**
     * Get the number of resource cards the player has
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public int getNumberResourceCards(int playerIndex) throws PlayerExistsException {
        return 0;
    }

    /**
     * Get the number of resources cards (specific type) the player has
     *
     * @param playerID
     * @param t
     * @return
     * @throws PlayerExistsException
     * @throws InvalidTypeException
     */
    @Override
    public int amountOwnedResource(int playerID, ResourceType t) throws PlayerExistsException, InvalidTypeException {
        return 0;
    }

    /**
     * Get the game winner
     *
     * @return
     * @throws GameOverException
     */
    @Override
    public Player getWinner() throws GameOverException {
        return null;
    }

    /**
     * Get the player's name
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public String getPlayerNameByIndex(int playerIndex) throws PlayerExistsException {
        return null;
    }

    /**
     * Get the player's color based on their name
     *
     * @param player
     * @return
     */
    @Override
    public CatanColor getPlayerColorByName(String player) {
        return null;
    }

    /**
     * Get the color of the specified player
     *
     * @param index index of the player
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public CatanColor getPlayerColorByIndex(int index) throws PlayerExistsException {
        return null;
    }
    //===============================================================================================
    //endregion

    //region Setters
    //=================================================================================================
    /**
     * Sets the game's id
     *
     * @param id
     */
    @Override
    public void setId(int id) {

    }
    //===================================================================================================
    //endregion

    //region Domestic trade methods
    //============================================================================
    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canTrade(int playerID) {
        return false;
    }

    @Override
    public boolean isTradeActive() {
        return false;
    }

    @Override
    public int getTradeReceiver() {
        return 0;
    }

    @Override
    public int getTradeSender() {
        return 0;
    }

    @Override
    public int getTradeBrick() {
        return 0;
    }

    @Override
    public int getTradeWood() {
        return 0;
    }

    @Override
    public int getTradeSheep() {
        return 0;
    }

    @Override
    public int getTradeWheat() {
        return 0;
    }

    @Override
    public int getTradeOre() {
        return 0;
    }
    //============================================================================================
    //endregion

    //region ResourceBar controller methods
    //======================================================================================
    @Override
    public boolean ableToBuildSettlement(int id) throws PlayerExistsException {
        return false;
    }

    @Override
    public boolean ableToBuildRoad(int id) throws PlayerExistsException {
        return false;
    }

    @Override
    public boolean ableToBuildCity(int id) throws PlayerExistsException {
        return false;
    }
    //=========================================================================================
    //endregion

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
}
