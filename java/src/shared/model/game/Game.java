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

public final class Game extends Observable implements IGame, JsonSerializable {
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

    //region Game methods
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
        assert players != null;
        assert this.playerManager != null;
        assert this.map != null;
        assert this.turnTracker != null;

        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        //Create the map
        this.map = new Map(randomHex, randomChits, randomPorts);
        //Create a new turn tracker
        turnTracker = new TurnTracker();

        //Return the first player's index
        return turnTracker.getCurrentTurn();
    }

    /**
     * Updates the game
     * @param json
     */
    public void updateGame(final JsonObject json) {
        assert json != null;
        assert json.has("deck");
        assert json.has("map");
        assert json.has("players");
        assert json.has("bank");
        assert json.has("turnTracker");
        assert json.has("chat");
        assert json.has("log");

        this.developmentCardBank = new DevelopmentCardBank(json.get("deck").getAsJsonObject(), true);
        this.map = new Map(json.get("map").getAsJsonObject());
        this.playerManager = new PlayerManager(json.get("players").getAsJsonArray());
        this.resourceCardBank = new ResourceCardBank(json.get("bank").getAsJsonObject(), true);

        // only update if someone actually has the longest road
        final JsonObject turnTracker = json.getAsJsonObject("turnTracker");
        int longestRoadIndex = turnTracker.get("longestRoad").getAsInt();
        if(longestRoadIndex >= 0) {
            this.longestRoadCard = new LongestRoad(longestRoadIndex);
        }

        this.largestArmyCard = new LargestArmy(turnTracker.get("largestArmy").getAsInt());
        this.version = json.get("version").getAsInt();
        this.winner = json.get("winner").getAsInt();
        if(json.has("tradeOffer")) {
            this.currentOffer = new Trade(json.get("tradeOffer").getAsJsonObject());
        } else {
            this.currentOffer = new Trade();
        }
        try {
            this.turnTracker = new TurnTracker(json.get("turnTracker").getAsJsonObject());
        } catch (BadJsonException e) {
            e.printStackTrace();
        }
        this.chat = new MessageList(json.get("chat").getAsJsonObject());
        this.log = new MessageList(json.get("log").getAsJsonObject());
        this.winner = json.get("winner").getAsInt();
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the current version of the game model
     * @return
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * Gets the playerIndex of the player who's turn it is
     *
     * @return
     */
    @Override
    public int getCurrentTurn() {
        return turnTracker.getCurrentTurn();
    }

    /**
     * Gets the phase of the current turn
     *
     * @return
     */
    @Override
    public TurnTracker.Phase getCurrentPhase() {
        return turnTracker.getPhase();
    }

    /**
     * Moves turn to the next phase
     */
    @Override
    public void nextPhase() {
        assert this.turnTracker != null;

        try {
            turnTracker.nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Can do methods
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert this.turnTracker != null;
        assert this.map != null;

        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.isSetupPhase() && map.canInitiateSettlement(playerIndex, vertex);
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return map.canBuildSettlement(playerIndex, vertex) && playerManager.canBuildSettlement(playerIndex) && turnTracker.canPlay();
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;

        return map.canBuildCity(playerIndex, vertex) && playerManager.canBuildCity(playerIndex) && turnTracker.canPlay();
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
        assert playerIndex >= 0;
        assert edge != null;
        assert this.turnTracker != null;
        assert this.map != null;

        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.isSetupPhase() && map.canInitiateRoad(playerIndex, edge);
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
        assert playerIndex >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getDir() != null;

        return (map.canBuildRoad(playerIndex, edge) && playerManager.canBuildRoad(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex));
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
        assert playerIndex >= 0;

        return playerManager.canDiscardCards(playerIndex);
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
        assert playerIndex >= 0;

        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.canRoll();
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
        assert playerIndex >= 0;

        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
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
        assert playerIndex >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return getCurrentTurn() == playerIndex && playerManager.canUseYearOfPlenty(playerIndex) && turnTracker.canPlay();
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
        assert playerIndex >= 0;
        return playerManager.canUseRoadBuilder(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
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
        assert playerIndex >= 0;

        return playerManager.canUseSoldier(playerIndex) && turnTracker.isPlayersTurn(playerIndex) && turnTracker.canPlay();
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
        assert playerIndex >= 0;

        return playerManager.canUseMonopoly(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
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
        assert playerIndex >= 0;

        return playerManager.canUseMonument(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
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
        assert playerIndex >= 0;

        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.canUseRobber() && map.canMoveRobber(hexLocation);
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
        assert playerIndex >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return playerManager.canBuyDevCard(playerIndex) && turnTracker.isPlayersTurn(playerIndex) && turnTracker.canPlay() && developmentCardBank.size() > 0;
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
        assert playerIndex >= 0;
        assert port != null;

        if(canOfferTrade(playerIndex)){
            Set<PortType> ports = getPortTypes(playerIndex);
            assert ports != null;
            if(ports.contains(port)){
                return playerManager.canMaritimeTrade(playerIndex, port);
            }
        }
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
        assert playerIndex >= 0;

        return turnTracker.isPlayersTurn(playerIndex);
    }
    //endregion

    //region Do methods
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert this.map != null;

        if(canInitiateSettlement(playerIndex, vertex)){
            map.initiateSettlement(playerIndex, vertex);
        }
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
        assert playerIndex >= 0;
        assert edge != null;
        assert this.map != null;

        map.initiateRoad(playerIndex, edge);
    }

    /**
     * Action - Player discards cards
     *
     * @param playerIndex Index of Player performing action
     * @param cards       Cards to be discarded
     */
    @Override
    public void discardCards(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        if(canDiscardCards(playerIndex) && this.turnTracker.canDiscard()) {
            playerManager.discardResourceType(playerIndex, cards);
        }
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
        assert playerIndexOne >= 0;
        assert playerIndexTwo >= 0;
        assert playerIndexOne != playerIndexTwo;
        assert playerOneCards != null;
        assert playerOneCards.size() > 0;
        assert playerTwoCards != null;
        assert playerTwoCards.size() > 0;
        assert !playerOneCards.equals(playerTwoCards);

        if(canOfferTrade(playerIndexOne)){
            final TradePackage one = new TradePackage(playerIndexOne,playerOneCards);
            final TradePackage two = new TradePackage(playerIndexTwo,playerTwoCards);

            // TODO - why is this trade object unused?
            Trade trade = new Trade(one,two);

            playerManager.offerTrade(playerIndexOne,playerIndexTwo,playerOneCards,playerTwoCards); //// TODO: 2/15/16 poorly named function.  OfferTrade shouldn't do the trade.

        }
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
        assert playerID >= 0;
        assert want1 != null;
        assert want2 != null;

        if(canUseYearOfPlenty(playerID)) {
            playerManager.useYearOfPlenty(playerID);
            ResourceCard rc1 = resourceCardBank.discard(want1);
            ResourceCard rc2 = resourceCardBank.discard(want2);
            playerManager.addResource(playerID, rc1);
            playerManager.addResource(playerID, rc2);
        }
    }

    /**
     * Action - Player finishes their turn
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public Integer finishTurn(int playerIndex) throws Exception {
        assert playerIndex >= 0;
        assert this.playerManager != null;

        try {
            playerManager.moveNewToOld(playerIndex);
        } catch (BadCallerException e) {
            e.printStackTrace();
        }

        return turnTracker.nextTurn();
    }
    //endregion

    //region Getters
    @Override
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public int getId() {
        return this.gameId;
    }
    //endregion

    //region Setters
    public void setId(int id) {
        this.gameId = id;
    }
    //endregion

    //region Player info
    @Override
    public CatanColor getPlayerColorByIndex(int id) throws PlayerExistsException {
        return null;
    }
    //endregion

    //region Testing methods only
    /**
     * Action - Player rolls the dice
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public int rollNumber(int playerIndex) throws InvalidDiceRollException {
        assert playerIndex >= 0;
        Dice dice = new Dice(2);

        int roll = dice.roll();
        if(roll == 7) {
            turnTracker.setPhase(TurnTracker.Phase.ROBBING);
        } else {
            map.getResources(roll);
        }

        try {
            turnTracker.nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roll;
    }
    //endregion


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

    @Override
    public void rob(int playerrobber, int playerrobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException {

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
     * builds a road for the player
     *
     * @param playerID
     * @param edge
     */
    @Override
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

    }

    /**
     * builds a settlement for this player
     *
     * @param playerID
     * @param vertex
     */
    @Override
    public void buildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

    }

    /**
     * builds a city for this player
     *
     * @param playerID
     * @param vertex
     */
    @Override
    public void buildCity(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {

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
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LongestRoad for playerIDNew and roadSize
     *
     * @param playerIDOld
     * @param playerIDNew
     * @param roadSize
     */
    @Override
    public void setPlayerWithLongestRoad(int playerIDOld, int playerIDNew, int roadSize) {

    }

    /**
     * returns the value of how many soldiers is the LargestArmy
     *
     * @return
     */
    @Override
    public int currentLargestArmySize() {
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
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LargestArmy for playerIDNew and armySize
     *
     * @param playerIDOld
     * @param playerIDNew
     * @param armySize
     */
    @Override
    public void newLargestArmy(int playerIDOld, int playerIDNew, int armySize) {

    }

    /**
     * Buys a new developmentCard for the player
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerID
     */
    @Override
    public DevCardType buyDevelopmentCard(int playerID) throws PlayerExistsException, Exception {
        return null;
    }

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

    /**
     * effectuates a trade based on the port type
     *
     * @param playerID
     * @param port
     * @param want
     */
    @Override
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {

    }

    @Override
    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {

    }

    @Override
    public Set<PortType> getPortTypes(int playerID) throws InvalidPlayerException {
        return null;
    }

    @Override
    public Map getMap() {
        return null;
    }

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

    @Override
    public Integer getAvailableRoads(int id) throws PlayerExistsException {
        return null;
    }

    @Override
    public Integer getAvailableSettlements(int id) throws PlayerExistsException {
        return null;
    }

    @Override
    public Integer getAvailableCities(int id) throws PlayerExistsException {
        return null;
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public Integer numberOfDevCard(int id) throws PlayerExistsException {
        return null;
    }

    @Override
    public Player getWinner() throws GameOverException {
        return null;
    }

    @Override
    public int getNumberDevCards(DevCardType type, int playerID) {
        return 0;
    }

    @Override
    public int getNumberResourceCards(int playerIndex) throws PlayerExistsException {
        return 0;
    }

    @Override
    public int amountOwnedResource(int playerID, ResourceType t) throws PlayerExistsException, InvalidTypeException {
        return 0;
    }

    @Override
    public void buildFirstRoad(int playerID, EdgeLocation hexloc) {

    }

    @Override
    public void cancelSoldierCard(int playerID) {

    }

    @Override
    public void deleteRoad(int playerID, EdgeLocation edge) {

    }

    @Override
    public void cancelRoadBuildingCard(int playerID) {

    }

    @Override
    public Player getPlayerById(int id) throws PlayerExistsException {
        return null;
    }

    @Override
    public int getNumberOfSoldiers(int playerIndex) {
        return 0;
    }

    @Override
    public boolean hasDiscarded(int playerIndex) {
        return false;
    }

    @Override
    public MessageList getLog() {
        return null;
    }

    @Override
    public CatanColor getPlayerColorByName(String player) {
        return null;
    }

    @Override
    public HashMap<ResourceType, Integer> getBankResources() {
        return null;
    }

    @Override
    public HashMap<ResourceType, Integer> getPlayerResources(int pIndex) throws PlayerExistsException {
        return null;
    }

    @Override
    public MessageList getChat() {
        return null;
    }

    @Override
    public int getPoints(int playerIndex) throws PlayerExistsException {
        return 0;
    }

    @Override
    public int getWinnerId() {
        return 0;
    }

    @Override
    public String getPlayerNameByIndex(int playerIndex) throws PlayerExistsException {
        return null;
    }

    @Override
    public int getPlayerIdByIndex(int playerIndex) throws PlayerExistsException {
        return 0;
    }

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
