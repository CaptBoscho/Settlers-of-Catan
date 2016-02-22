package shared.model.game;

import client.data.GameInfo;
import client.data.PlayerInfo;
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


    private static Game instance;

    private int gameId;
    private Dice dice;
    private Map map;
    private TurnTracker turnTracker;
    private LongestRoad longestRoadCard;
    private LargestArmy largestArmyCard;
    private PlayerManager playerManager;
    private ResourceCardBank resourceCardBank;
    private DevelopmentCardBank developmentCardBank;
    private int winner;
    private int version;

    //private List<Observer> observers = new ArrayList<Observer>();

    /**
     * Constructor
     */
    protected Game() {
        this.dice = new Dice(2);
        this.map = new Map(false, false, false);
        this.turnTracker = new TurnTracker();
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager(new ArrayList<>());
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);

    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }

        return instance;
    }


    public void reset() {
        this.dice = new Dice(2);
        this.map = new Map(false, false, false);
        this.turnTracker = new TurnTracker();
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager(new ArrayList<>());
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);
    }

    public void updateGame(final JsonObject json) {
        assert json != null;
        assert json.has("deck");
        assert json.has("map");
        assert json.has("players");
        assert json.has("bank");
        assert json.has("turnTracker");

        this.developmentCardBank = new DevelopmentCardBank(json.get("deck").getAsJsonObject(), true);
        this.map = new Map(json.get("map").getAsJsonObject());
        this.playerManager = new PlayerManager(json.get("players").getAsJsonArray());
        this.resourceCardBank = new ResourceCardBank(json.get("bank").getAsJsonObject(), true);

        // only update if someone actually has the longest road
        final JsonObject turnTracker = json.getAsJsonObject("turnTracker");
        int longestRoadId = turnTracker.get("longestRoad").getAsInt();
        if(longestRoadId >= 0) {
            this.longestRoadCard = new LongestRoad(longestRoadId);
        }

        this.largestArmyCard = new LargestArmy(turnTracker.get("largestArmy").getAsInt());
        this.version = json.get("version").getAsInt();
        this.winner = json.get("winner").getAsInt();
        try {
            this.turnTracker = new TurnTracker(json.get("turnTracker").getAsJsonObject());
        } catch (BadJsonException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    public void updateGame(ClientModel model) {
        // TODO --
    }

    public int getVersion() {
        return this.version;
    }

    //IGame Methods
    //======================================================
    /**
     * Starts the game, returns the Id for the first player
     *
     * @param players
     * @return Id of first player
     */
    public int initializeGame(List<Player> players, boolean randomhexes, boolean randomchits, boolean randomports) throws FailedToRandomizeException {
        assert players != null;
        assert this.playerManager != null;
        assert this.map != null;
        assert this.turnTracker != null;

        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        this.map = new Map(randomhexes, randomchits, randomports);
        //List<Integer> order = this.playerManager.randomizePlayers();
        turnTracker = new TurnTracker();

        return turnTracker.getCurrentTurn();
    }

    public boolean canInitiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException {
        assert playerID >= 0;
        assert vertex != null;
        assert this.turnTracker != null;
        assert this.map != null;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase() && map.canInitiateSettlement(playerID, vertex);
    }

    public void initiateSettlement(int playerID, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException {
        assert playerID >= 0;
        assert vertex != null;
        assert this.map != null;

        if(canInitiateSettlement(playerID, vertex)){
            map.initiateSettlement(playerID, vertex);
        }
    }

    public boolean canInitiateRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException {
        assert playerID >= 0;
        assert edge != null;
        assert this.turnTracker != null;
        assert this.map != null;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase() && map.canInitiateRoad(playerID, edge);
    }

    public void initiateRoad(int playerID, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException {
        assert playerID >= 0;
        assert edge != null;
        assert this.map != null;

        map.initiateRoad(playerID, edge);
    }

    /**
     * returns the playerID for whose turn it is
     *
     * @return
     */
    @Override
    public int getCurrentTurn() {
        assert this.turnTracker != null;

        return turnTracker.getCurrentTurn();
    }

    /**
     * Determine if Player can discard cards
     * Checks resource cards, robber position,
     * and hexes from dice roll
     *
     * @param playerID ID of Player performing action
     * @return True if Player can discard cards
     */
    @Override
    public boolean canDiscardCards(int playerID) throws PlayerExistsException {
        assert playerID >= 0;

        return playerManager.canDiscardCards(playerID);

    }

    /**
     * Action - Player discards cards
     *
     * @param playerID ID of Player performing action
     * @param cards    Cards to be discarded
     */
    @Override
    public void discardCards(int playerID, List<ResourceType> cards) throws PlayerExistsException, InvalidTypeException, InsufficientResourcesException{
        if(canDiscardCards(playerID) && this.turnTracker.canDiscard()) {
            playerManager.discardResourceType(playerID, cards);
        }
    }

    /**
     * Determine if Player can roll the dice
     * Checks Player turn and phase of turn
     *
     * @param playerID ID of Player performing action
     * @return True if Player can roll the dice
     */
    @Override
    public boolean canRollNumber(int playerID) {
        assert playerID >= 0;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.canRoll();
    }

    /**
     * Action - Player rolls the dice
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public int rollNumber(int playerID) throws InvalidDiceRollException {
        assert playerID >= 0;

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

    /**
     * Determine if Player can offer a trade
     * Checks Player turn, phase, and resources
     *
     * @param playerID ID of Player performing action
     * @return True if Player can offer a trade
     */
    @Override
    public boolean canOfferTrade(int playerID) {
        assert playerID >= 0;

        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
    }

    /**
     * for testing purposes
     * @param card
     * @param id
     * @throws PlayerExistsException
     */
    public void giveResource(ResourceCard card, int id) throws PlayerExistsException {
        assert card != null;

        playerManager.addResource(id, card);
    }

    /**
     * for testing purposes.
     * @param t
     * @return
     * @throws InsufficientResourcesException
     * @throws InvalidTypeException
     */
    public ResourceCard getResourceCard(final ResourceType t) throws InsufficientResourcesException, InvalidTypeException {
        assert t != null;

        return resourceCardBank.discard(t);
    }

    public int amountOwnedResource(final int playerID, final ResourceType t) throws PlayerExistsException, InvalidTypeException {
        assert playerID >= 0;
        assert t != null;

        return playerManager.getPlayerByID(playerID).howManyOfThisCard(t);
    }

    /**
     * Action - Player offers trade
     *
     * @param playerIDOne   ID of Player offering the trade
     * @param playerIDTwo ID of Player being offered the trade
     */
    @Override
    public void offerTrade(int playerIDOne, int playerIDTwo, List<ResourceType> onecards, List<ResourceType> twocards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException{
        assert playerIDOne >= 0;
        assert playerIDTwo >= 0;
        assert playerIDOne != playerIDTwo;
        assert onecards != null;
        assert onecards.size() > 0;
        assert twocards != null;
        assert twocards.size() > 0;
        assert !onecards.equals(twocards);

        if(canOfferTrade(playerIDOne)){
            final TradePackage one = new TradePackage(playerIDOne,onecards);
            final TradePackage two = new TradePackage(playerIDTwo, twocards);

            // TODO - why is this trade object unused?
            Trade trade = new Trade(one,two);

            playerManager.offerTrade(playerIDOne,playerIDTwo,onecards,twocards); //// TODO: 2/15/16 poorly named function.  OfferTrade shouldn't do the trade.

        }
    }


    /**
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     *
     * @param playerID ID of Player performing action
     * @return True if Player can finish their turn
     */
    @Override
    public boolean canFinishTurn(int playerID) {
        assert playerID >= 0;

        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
    }

    /** 
     * Action - Player finishes their turn
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public Integer finishTurn(int playerID) throws Exception {
        assert playerID >= 0;

        return turnTracker.nextTurn();
    }

    public TurnTracker.Phase getCurrentPhase(){
        return turnTracker.getPhase();
    }

    public void nextPhase() {
        assert this.turnTracker != null;

        try {
            turnTracker.nextPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPhase(TurnTracker.Phase p) {
        assert this.turnTracker != null;

        turnTracker.setPhase(p);
    }

    /**
     * checks if the player has the cards to buy a DevelopmentCard
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuyDevelopmentCard(final int playerID) throws PlayerExistsException {
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return playerManager.canBuyDevCard(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();
    }

        /**
         * Action - Player buys a dev card
         *
         * @param playerID ID of Player performing action
         */
    @Override
    public DevCardType buyDevelopmentCard(int playerID) throws Exception {
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.developmentCardBank != null;

        playerManager.buyDevCard(playerID);
        final DevelopmentCard dc = developmentCardBank.draw();
        playerManager.addDevCard(playerID, dc);
        return dc.getType();
    }

    /**
     * Determine if Player can play Year of Plenty
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Year of Plenty
     */
    @Override
    public boolean canUseYearOfPlenty(int playerID) throws PlayerExistsException {
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return getCurrentTurn() == playerID && playerManager.canUseYearOfPlenty(playerID) && turnTracker.canPlay();
    }

    public void addDevCard(final DevelopmentCard dc, final int playerID) throws PlayerExistsException {
        assert dc != null;
        assert playerID >= 0;
        assert this.playerManager != null;

        playerManager.getPlayerByID(playerID).addDevCard(dc);
    }

    public Integer numberOfDevCard(final int playerID) throws PlayerExistsException {
        assert playerID >= 0;
        assert this.playerManager != null;

        return playerManager.getPlayerByID(playerID).quantityOfDevCards();
    }

    public void moveNewToOld(int playerID) throws PlayerExistsException, BadCallerException {
        assert playerID >= 0;
        assert this.playerManager != null;

        playerManager.moveNewToOld(playerID);
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerID ID of Player performing action
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
     * Determine if Player can play Road Builder
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Road Builder
     */
    @Override
    public boolean canUseRoadBuilder(int playerID) throws PlayerExistsException {
        assert playerID >= 0;
        return playerManager.canUseRoadBuilder(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException {
        assert playerID >= 0;
        assert edge1 != null;
        assert edge1.getHexLoc() != null;
        assert edge1.getHexLoc().getX() >= 0;
        assert edge1.getHexLoc().getY() >= 0;
        assert edge2 != null;
        assert edge2.getHexLoc() != null;
        assert edge2.getHexLoc().getX() >= 0;
        assert edge2.getHexLoc().getY() >= 0;
        assert !edge1.equals(edge2);

        if(canUseRoadBuilder(playerID)){
            playerManager.useRoadBuilder(playerID);
            buildRoad(playerID, edge1);
            buildRoad(playerID, edge2);
        }
    }

    /**
     * Determine if Player can play Soldier
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Soldier
     */
    @Override
    public boolean canUseSoldier(int playerID) throws PlayerExistsException{
        assert playerID >= 0;

        return playerManager.canUseSoldier(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();

    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public Set<Integer> useSoldier(int playerID, HexLocation hexloc) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException {
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.largestArmyCard != null;
        assert this.turnTracker != null;

        if(canUseSoldier(playerID)) {
            playerManager.useSoldier(playerID);
            int used = playerManager.getKnights(playerID);
            if(used >= 3 && used > largestArmyCard.getMostSoldiers()) {
                final int oldPlayer = largestArmyCard.getOwner();
                largestArmyCard.setNewOwner(playerID, used);
                playerManager.changeLargestArmyPossession(oldPlayer, playerID);
            }

            turnTracker.setPhase(TurnTracker.Phase.ROBBING);
            if(canPlaceRobber(playerID, hexloc)) {
                return placeRobber(playerID, hexloc);
            }
            return null;
        }
        return null;
    }

    /**
     * Determine if Player can play Monopoly
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monopoly
     */
    @Override
    public boolean canUseMonopoly(final int playerID) throws PlayerExistsException {
        assert playerID >= 0;

        return playerManager.canUseMonopoly(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonopoly(final int playerID, final ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerID >= 0;
        assert type != null;

        if(canUseMonopoly(playerID)) {
            playerManager.useMonopoly(playerID, type);
        }
    }

    /**
     * Determine if Player can play Monument
     * Checks Player turn, and dev cards
     *
     * @param playerID ID of Player performing action
     * @return True if Player can play Monument
     */
    @Override
    public boolean canUseMonument(final int playerID) throws PlayerExistsException {
        assert playerID >= 0;

        return playerManager.canUseMonument(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);

    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(final int playerID) throws PlayerExistsException, DevCardException {
        assert playerID >= 0;

        if(canUseMonument(playerID)){
            playerManager.useMonument(playerID);
        }
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerID ID of Player performing action
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(final int playerID, HexLocation hexloc) {
        assert playerID >= 0;
        try {

            return turnTracker.isPlayersTurn(playerID) && turnTracker.canUseRobber() && map.canMoveRobber(hexloc);

        } catch(InvalidLocationException e){
            return false;
        }

    }
    /**
     * Action - Player places the Robber
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException {
        assert playerID >= 0;
        assert hexloc != null;

        return map.moveRobber(playerID, hexloc);
    }

    //TODO change robbing to check if the phase in the turntracker is robbing phase
    public void rob(int playerRobber, int playerRobbed) throws MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException{
        assert playerRobbed > 0;
        assert playerRobber > 0;
        assert playerRobbed != playerRobber;
        assert this.map != null;
        assert this.turnTracker != null;
        assert this.playerManager != null;

        Set<Integer> who = map.whoCanGetRobbed(playerRobber);
        assert who != null;
        if(turnTracker.canPlay() && turnTracker.isPlayersTurn(playerRobber) && turnTracker.canUseRobber() && who.contains(playerRobbed)){
            turnTracker.setPhase(TurnTracker.Phase.ROBBING);
            ResourceType stolenResource = playerManager.placeRobber(playerRobber, playerRobbed);

        }
    }

    /**
     * returns boolean value denoting if the player can build a
     * road (just checks cards really)
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerID >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getHexLoc().getX() >= 0;
        assert edge.getHexLoc().getY() >= 0;
        assert edge.getDir() != null;

        // if(turnTracker.canBuild(playerID)){
            return (map.canBuildRoad(playerID, edge) && playerManager.canBuildRoad(playerID) && turnTracker.canPlay());
       // }
       // return false;
    }

    public boolean ableToBuildRoad(int id) throws PlayerExistsException{
        return(playerManager.canBuildRoad(id) && turnTracker.canPlay());
    }

    public boolean ableToBuildSettlement(int id) throws PlayerExistsException{
        return(playerManager.canBuildSettlement(id) && turnTracker.canPlay());
    }

    public boolean ableToBuildCity(int id) throws PlayerExistsException{
        return(playerManager.canBuildCity(id) && turnTracker.canPlay());
    }

    public Integer getAvailableRoads(int id) throws PlayerExistsException{
        return this.playerManager.getAvailableRoads(id);
    }

    public Integer getAvailableSettlements(int id) throws PlayerExistsException{
        return this.playerManager.getAvailableSettlements(id);
    }

    public Integer getAvailableCities(int id) throws PlayerExistsException{
        return this.playerManager.getAvailableCities(id);
    }

    /**
     * builds a road for hte player
     *
     * @param playerID
     */
    @Override
    public void buildRoad(int playerID, EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        assert playerID >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getHexLoc().getX() >= 0;
        assert edge.getHexLoc().getY() >= 0;
        assert edge.getDir() != null;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.longestRoadCard != null;

        if(canBuildRoad(playerID, edge)) {
            map.buildRoad(playerID, edge);
            playerManager.buildRoad(playerID);
            //check to update longest road
            int roadlength = map.getLongestRoadSize(playerID);
            if(roadlength >= 5 && roadlength > longestRoadCard.getSize()){
                newLongestRoad(longestRoadCard.getOwner(), playerID, roadlength);
            }
        }
    }

    /**
     * checks if the player has the cards to build a settlement
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildSettlement(int playerID, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert vertex.getHexLoc().getX() >= 0;
        assert vertex.getHexLoc().getY() >= 0;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return map.canBuildSettlement(playerID, vertex) && playerManager.canBuildSettlement(playerID) && turnTracker.canPlay(); //&& turnTracker.canBuild(playerID);
    }

    /**
     * builds a settlement for this player
     *
     * @param playerID
     */
    @Override
    public void buildSettlement(final int playerID, final VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert vertex.getHexLoc().getX() >= 0;
        assert vertex.getHexLoc().getY() >= 0;
        assert this.map != null;
        assert this.playerManager != null;

        if(canBuildSettlement(playerID, vertex)) {
            map.buildSettlement(playerID, vertex);
            playerManager.buildSettlement(playerID);
        }
    }

    /**
     * checks if the player has the cards to build a city
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canBuildCity(final int playerID, final VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert vertex.getHexLoc().getX() >= 0;
        assert vertex.getHexLoc().getY() >= 0;

        return map.canBuildCity(playerID, vertex) && playerManager.canBuildCity(playerID) && turnTracker.canPlay(); //&& turnTracker.canBuild(playerID);
    }

    /**
     * builds a city for this player
     *
     * @param playerID
     */
    @Override
    public void buildCity(final int playerID, final VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert vertex.getHexLoc().getX() >= 0;
        assert vertex.getHexLoc().getY() >= 0;

        if(canBuildCity(playerID, vertex)) {
            map.buildCity(playerID, vertex);
            playerManager.buildCity(playerID);
        }
    }

    /**
     * returns the value of how many roads is the LongestRoad
     *
     * @return
     */
    @Override
    public int currentLongestRoadSize() {
        return longestRoadCard.getSize();
    }

    /**
     * returns the playerID of who owns the current longest road
     *
     * @return
     */
    @Override
    public int currentLongestRoadPlayer() {
        return longestRoadCard.getOwner();
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
    public void newLongestRoad(int playerIDOld, int playerIDNew, int roadSize) {
        assert playerIDNew >= 0;
        assert playerIDOld >= 0;
        assert roadSize >= 0;
        assert playerIDNew != playerIDOld;

        longestRoadCard.setOwner(playerIDNew, roadSize);
    }

    /**
     * returns the value of how many soldiers is the LargestArmy
     *
     * @return
     */
    @Override
    public int currentLargestArmySize() {
        return largestArmyCard.getMostSoldiers();
    }

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    @Override
    public int currentLargestArmyPlayer() {
        return largestArmyCard.getOwner();
    }

    /**
     * deducts Victory Points from playerIDOld
     * adds Victory Points to playerIDNew
     * Updates LargestArmy for playerIDNew and armySize
     * @param playerIDOld
     * @param playerIDNew
     * @param armySize
     */
    public void newLargestArmy(int playerIDOld, int playerIDNew, int armySize){
        assert playerIDNew >= 0;
        assert playerIDOld >= 0;
        assert armySize >= 0;
        assert playerIDNew != playerIDOld;

        largestArmyCard.setNewOwner(playerIDNew, armySize);
    }

    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerID
     * @return
     */
    @Override
    public boolean canTrade(int playerID) {
        assert playerID >= 0;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();
    }

    /**
     * checks if that player has the card needed for that port's trade
     *
     * @param playerID
     * @param port
     * @return
     */
    @Override
    public boolean canMaritimeTrade(int playerID, PortType port) throws InvalidPlayerException, PlayerExistsException {
        assert playerID >= 0;
        assert port != null;

        if(canTrade(playerID)){
            Set<PortType> ports = getPortTypes(playerID);
            assert ports != null;
            if(ports.contains(port)){
                return playerManager.canMaritimeTrade(playerID, port);
            }
        }
        return false;
    }

    /**
     * effectuates a trade based on the port type
     *
     * @param playerID
     * @param port
     */
    @Override
    public void maritimeTrade(int playerID, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerID >= 0;
        assert port != null;
        assert want != null;

        if(canMaritimeTrade(playerID, port)){
            List<ResourceType> cards = new ArrayList<>();
            switch(port) {
                case BRICK:
                    cards.add(ResourceType.BRICK);
                    cards.add(ResourceType.BRICK);
                    break;
                case ORE:
                    cards.add(ResourceType.ORE);
                    cards.add(ResourceType.ORE);
                    break;
                case SHEEP:
                    cards.add(ResourceType.SHEEP);
                    cards.add(ResourceType.SHEEP);
                    break;
                case WHEAT:
                    cards.add(ResourceType.WHEAT);
                    cards.add(ResourceType.WHEAT);
                    break;
                case WOOD:
                    cards.add(ResourceType.WOOD);
                    cards.add(ResourceType.WOOD);
                    break;
            }
            final List<ResourceCard> discarded = playerManager.discardResourceType(playerID, cards);
            assert discarded != null;
            for(ResourceCard rc: discarded) {
                resourceCardBank.addResource(rc);
            }

            playerManager.addResource(playerID, resourceCardBank.discard(want));
        }
    }

    public void maritimeTradeThree(int playerID, PortType port, ResourceType give, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert playerID >=0 ;
        assert port != null;
        assert give != null;
        assert want != null;

        if(canMaritimeTrade(playerID, port)) {
            if(port == PortType.THREE) {
                final List<ResourceType> cards = new ArrayList<>();
                cards.add(give);
                cards.add(give);
                cards.add(give);

                final List<ResourceCard> discarded = playerManager.discardResourceType(playerID, cards);

                for(final ResourceCard rc: discarded) {
                    resourceCardBank.addResource(rc);
                }

                playerManager.addResource(playerID, resourceCardBank.discard(want));

            } else {
                throw new InvalidTypeException("not 3:1 port types");
            }
        }
    }



    public Set<PortType> getPortTypes(int playerID) {
        assert playerID >= 0;
        assert this.map != null;

        return map.getPortTypes(playerID);
    }

    public TurnTracker getTurnTracker() {
        return turnTracker;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Map getMap() {
        return this.map;
    }

    public CatanColor getPlayerColorByID(int id) throws PlayerExistsException {
        return this.playerManager.getPlayerColorByID(id);
    }

    public List<Player> getPlayers() {
        return this.playerManager.getPlayers();
    }

    public Player getWinner() throws GameOverException {
        return playerManager.getWinner();
    }

    public void buildFirstRoad(int playerID, EdgeLocation hexloc){
        if(!this.turnTracker.isPlayersTurn(playerID)){return;}
        if(!this.turnTracker.canPlay()){return;}
        try {
            this.map.buildRoad(playerID, hexloc);
        }catch(InvalidLocationException e){}
        catch(StructureException e){}
    }

    public void cancelSoldierCard(int playerID){
        try {
            SoldierCard sc = new SoldierCard();
            this.playerManager.addDevCard(playerID, sc);
        }catch(PlayerExistsException e){}
    }

    public void deleteRoad(int playerID, EdgeLocation edge){
        if(!this.turnTracker.isPlayersTurn(playerID)){return;}
        if(!this.turnTracker.canPlay()){return;}
        try {
            this.map.deleteRoad(playerID, edge);
        }catch(InvalidLocationException e){}
        catch(StructureException e){}
    }

    public void cancelRoadBuildingCard(int playerID){
        try {
            RoadBuildCard rbc = new RoadBuildCard();
            this.playerManager.addDevCard(playerID, rbc);
        }catch(PlayerExistsException e){}
    }

    /*======================================================
    * Private - Helper Methods
    * ======================================================*/
    /**
     * Randomize the players' turn order
     */
    private void randomizePlayers() {
        assert this.playerManager != null;

        try{
            playerManager.randomizePlayers();
        } catch(Exception e) {
            //throw new
        }
    }

    @Override
    public JsonObject toJSON() {
        return null;
    }
}
