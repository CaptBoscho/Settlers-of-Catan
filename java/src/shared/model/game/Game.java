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

    //region Constructors
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

    /**
     * Updates the game
     *
     * @param json
     */
    @Override
    public void updateGame(JsonObject json) {
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
     *
     * @return
     */
    @Override
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
        assert this.turnTracker != null;

        return turnTracker.getCurrentTurn();
    }

    /**
     * Get all of the players in the game
     *
     * @return
     */
    @Override
    public List<Player> getPlayers() {
        return playerManager.getPlayers();
    }

    /**
     * Build the first road as part of the road building card
     *
     * @param playerIndex
     * @param location
     */
    @Override
    public void buildFirstRoad(int playerIndex, EdgeLocation location) {
        try{
            if(!this.turnTracker.isPlayersTurn(playerID)){return;}
            if(!this.turnTracker.canPlay()){return;}

            this.map.buildRoad(playerID, hexloc);
        } catch(InvalidLocationException | StructureException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a road from the map
     *
     * @param playerIndex
     * @param edge
     */
    @Override
    public void deleteRoad(int playerIndex, EdgeLocation edge) {
        try{
            if(!this.turnTracker.isPlayersTurn(playerID)){return;}
            if(!this.turnTracker.canPlay()){return;}

            this.map.deleteRoad(playerID, edge);
        } catch(InvalidLocationException | StructureException e) {
            e.printStackTrace();
        }
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
        assert playerID >= 0;
        assert vertex != null;
        assert this.turnTracker != null;
        assert this.map != null;
        return turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase() && map.canInitiateSettlement(playerID, vertex);
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
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return map.canBuildSettlement(playerID, vertex) && playerManager.canBuildSettlement(playerID) && turnTracker.canPlay();
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
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;

        return map.canBuildCity(playerID, vertex) && playerManager.canBuildCity(playerID) && turnTracker.canPlay(); //&& turnTracker.canBuild(playerID);
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
        assert playerID >= 0;
        assert edge != null;
        assert this.turnTracker != null;
        assert this.map != null;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.isSetupPhase() && map.canInitiateRoad(playerID, edge);
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
        assert playerID >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getDir() != null;

        return (map.canBuildRoad(playerID, edge) && playerManager.canBuildRoad(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID));
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
        assert playerID >= 0;

        return playerManager.canDiscardCards(playerID);
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
        assert playerID >= 0;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.canRoll();
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
        return false; assert playerID >= 0;

        return turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return getCurrentTurn() == playerID && playerManager.canUseYearOfPlenty(playerID) && turnTracker.canPlay();
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
        assert playerID >= 0;

        return playerManager.canUseRoadBuilder(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
        assert playerID >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getDir() != null;

        return (map.canBuildRoad(playerID, edge));
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
        assert playerID >= 0;

        return playerManager.canUseSoldier(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();
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
        assert playerID >= 0;

        return playerManager.canUseMonopoly(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
        assert playerID >= 0;

        return playerManager.canUseMonument(playerID) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerID);
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
        assert playerID >= 0;

        return turnTracker.isPlayersTurn(playerID) && turnTracker.canUseRobber() && map.canMoveRobber(hexloc);
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
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.turnTracker != null;

        return playerManager.canBuyDevCard(playerID) && turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay() && developmentCardBank.size() > 0;
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
     * Determine if Player can finish their turn
     * Checks Player turn and phase
     *
     * @param playerIndex Index of Player performing action
     * @return True if Player can finish their turn
     */
    @Override
    public boolean canFinishTurn(int playerIndex) {
        assert playerID >= 0;

        return turnTracker.isPlayersTurn(playerID);
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
        assert playerID >= 0;
        assert vertex != null;
        assert this.map != null;

        if(canInitiateSettlement(playerID, vertex)){
            map.initiateSettlement(playerID, vertex);
        }
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
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert this.map != null;
        assert this.playerManager != null;

        if(canBuildSettlement(playerID, vertex)) {
            map.buildSettlement(playerID, vertex);
            playerManager.buildSettlement(playerID);
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
        assert playerID >= 0;
        assert edge != null;
        assert this.map != null;

        map.initiateRoad(playerID, edge);
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
        assert playerID >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
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
                setPlayerWithLongestRoad(longestRoadCard.getOwner(), playerID, roadlength);
            }
        }
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
        assert playerID >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;

        if(canBuildCity(playerID, vertex)) {
            map.buildCity(playerID, vertex);
            playerManager.buildCity(playerID);
        }
    }

    /**
     * Action - Player discards cards
     *
     * @param playerIndex Index of Player performing action
     * @param cards       Cards to be discarded
     */
    @Override
    public void discardCards(int playerIndex, List<ResourceType> cards) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        if(canDiscardCards(playerID) && this.turnTracker.canDiscard()) {
            playerManager.discardResourceType(playerID, cards);
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
     * Action - Player plays Road Builder
     *
     * @param playerID ID of Player performing action
     * @param edge1
     * @param edge2
     */
    @Override
    public void useRoadBuilder(int playerID, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException {
        assert playerID >= 0;
        assert edge1 != null;
        assert edge1.getHexLoc() != null;
        assert edge2 != null;
        assert edge2.getHexLoc() != null;
        assert !edge1.equals(edge2);

        if(canUseRoadBuilding(playerID)) {
            playerManager.useRoadBuilder(playerID);
            buildRoad(playerID, edge1);
            buildRoad(playerID, edge2);
        }
    }

    /**
     * Cancels playing the road builder
     * @param playerIndex
     */
    public void cancelRoadBuildingCard(int playerIndex) {
        try {
            RoadBuildCard rbc = new RoadBuildCard();
            this.playerManager.addDevCard(playerIndex, rbc);
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action - Player plays Soldier
     *
     * @param playerID ID of Player performing action
     * @param hexloc
     */
    @Override
    public Set<Integer> useSoldier(int playerID, HexLocation hexloc) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException {
        assert playerIndex >= 0;
        assert this.playerManager != null;
        assert this.largestArmyCard != null;
        assert this.turnTracker != null;

        if(canUseSoldier(playerIndex)) {
            playerManager.useSoldier(playerIndex);
            int used = playerManager.getNumberOfSoldiers(playerIndex);
            if(used >= 3 && used > largestArmyCard.getMostSoldiers()) {
                final int oldPlayer = largestArmyCard.getOwner();
                largestArmyCard.setNewOwner(playerIndex, used);
                playerManager.changeLargestArmyPossession(oldPlayer, playerIndex);
            }

            turnTracker.setPhase(TurnTracker.Phase.ROBBING);
            if(canPlaceRobber(playerIndex, hexloc)) {
                return placeRobber(playerIndex, hexloc);
            }
            return null;
        }
        return null;
    }

    /**
     * Cancels playing a soldier card
     * @param playerIndex
     */
    public void cancelSoldierCard(int playerIndex){
        try {
            SoldierCard sc = new SoldierCard();
            this.playerManager.addDevCard(playerID, sc);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerID ID of Player performing action
     * @param type
     */
    @Override
    public void useMonopoly(int playerID, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerID >= 0;
        assert type != null;

        if(canUseMonopoly(playerID)) {
            playerManager.useMonopoly(playerID, type);
        }
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerID ID of Player performing action
     */
    @Override
    public void useMonument(int playerID) throws PlayerExistsException, DevCardException {
        assert playerID >= 0;

        if(canUseMonument(playerID)){
            playerManager.useMonument(playerID);
        }
    }

    /**
     * Action - Player places the Robber
     *
     * @param playerID ID of Player performing action
     * @param hexloc
     */
    @Override
    public Set<Integer> placeRobber(int playerID, HexLocation hexloc) throws AlreadyRobbedException, InvalidLocationException {
        assert playerID >= 0;
        assert hexloc != null;
        return map.moveRobber(playerID, hexloc);
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
        assert playerRobbed > 0;
        assert playerRobber > 0;
        assert playerRobbed != playerRobber;
        assert this.map != null;
        assert this.turnTracker != null;
        assert this.playerManager != null;

        Set<Integer> who = map.whoCanGetRobbed(playerRobber);
        assert who != null;
        if(turnTracker.canPlay() && turnTracker.isPlayersTurn(playerRobber) && turnTracker.canUseRobber() && who.contains(playerRobbed)){
            turnTracker.setPhase(TurnTracker.Phase.ROBBING); //TODO look at this statement
            ResourceType stolenResource = playerManager.placeRobber(playerRobber, playerRobbed);

        }
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
        assert playerID >= 0;
        assert this.playerManager != null;
        assert this.developmentCardBank != null;

        playerManager.buyDevCard(playerID);
        final DevelopmentCard dc = developmentCardBank.draw();
        playerManager.addDevCard(playerID, dc);
        return dc.getType();
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
        return this.playerManager;
    }

    /**
     * Gets the instance of the turn tracker
     *
     * @return
     */
    @Override
    public TurnTracker getTurnTracker() {
        return turnTracker;
    }

    /**
     * Gets the map instance
     *
     * @return
     */
    @Override
    public Map getMap() {
        return this.map;
    }

    /**
     * Gets the chat
     *
     * @return
     */
    @Override
    public MessageList getChat() {
        return this.chat;
    }

    /**
     * Gets the game's id
     *
     * @return
     */
    @Override
    public int getId() {
        return this.gameId;
    }

    /**
     * Get the player with the longest road card
     *
     * @return
     */
    @Override
    public int getPlayerWithLongestRoad() {
        return longestRoadCard.getOwner();
    }

    /**
     * returns the playerID of who owns the current largest army
     *
     * @return
     */
    @Override
    public int getPlayerWithLargestArmy() {
        return largestArmyCard.getOwner();
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
        assert playerID >= 0;
        assert this.map != null;

        return map.getPortTypes(playerID);
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
        return playerManager.getAvailableRoads(id);
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
        return playerManager.getAvailableSettlements(id);
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
        return playerManager.getAvailableCities(id);
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
        return playerManager.getPlayerByID(id);
    }

    /**
     * Get the number of soldiers the specified player has
     *
     * @param playerIndex
     * @return
     */
    @Override
    public int getNumberOfSoldiers(int playerIndex) {
        return playerManager.getNumberOfSoldiers(playerIndex);
    }

    /**
     * Check if the player has discarded this phase
     *
     * @param playerIndex
     * @return
     */
    @Override
    public boolean hasDiscarded(int playerIndex) {
        return playerManager.hasDiscarded(playerIndex);
    }

    /**
     * Get the game log - list of events that have occured in the game
     *
     * @return
     */
    @Override
    public MessageList getLog() {
        return this.log;
    }

    /**
     * Get the resources left in the bank
     *
     * @return
     */
    @Override
    public HashMap<ResourceType, Integer> getBankResources() {
        HashMap<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK,this.resourceCardBank.getNumberOfBrick());
        resources.put(ResourceType.WOOD, this.resourceCardBank.getNumberOfWood());
        resources.put(ResourceType.ORE, this.resourceCardBank.getNumberOfOre());
        resources.put(ResourceType.WHEAT, this.resourceCardBank.getNumberOfWheat());
        resources.put(ResourceType.SHEEP, this.resourceCardBank.getNumberOfSheep());
        return resources;
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
        HashMap<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK,this.playerManager.getPlayerByIndex(pIndex).getResourceCardBank().getNumberOfBrick());
        resources.put(ResourceType.WOOD,this.playerManager.getPlayerByIndex(pIndex).getResourceCardBank().getNumberOfWood());
        resources.put(ResourceType.ORE,this.playerManager.getPlayerByIndex(pIndex).getResourceCardBank().getNumberOfOre());
        resources.put(ResourceType.WHEAT,this.playerManager.getPlayerByIndex(pIndex).getResourceCardBank().getNumberOfWheat());
        resources.put(ResourceType.SHEEP,this.playerManager.getPlayerByIndex(pIndex).getResourceCardBank().getNumberOfSheep());
        return resources;
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
        Player player = playerManager.getPlayerByIndex(playerIndex);
        int totalPoints = 0;
        totalPoints += player.getVictoryPoints();
        return totalPoints;
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
        assert playerID >= 0;
        assert this.playerManager != null;

        return playerManager.getPlayerByIndex(playerID).quantityOfDevCards();
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
        return playerManager.getNumberDevCards(type, playerID);
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
        assert playerIndex >= 0;

        return playerManager.getNumberResourceCards(playerIndex);
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
        assert playerID >= 0;
        assert t != null;

        return playerManager.getPlayerByIndex(playerID).howManyOfThisCard(t);
    }

    /**
     * Get the game winner
     *
     * @return
     * @throws GameOverException
     */
    @Override
    public Player getWinner() throws GameOverException {
        return playerManager.getWinner();
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
        return playerManager.getPlayerByIndex(playerIndex).getName();
    }

    /**
     * Get the player's color based on their name
     *
     * @param player
     * @return
     */
    @Override
    public CatanColor getPlayerColorByName(String player) {
        assert player != null;
        return playerManager.getPlayerColorByName(player);
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
        return this.playerManager.getPlayerColorByIndex(index);
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
        this.gameId = id;
    }

    /**
     * Sets the game's player manager
     * @param playerManager
     */
    @Override
    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
        setChanged();
        this.notifyObservers();
    }

    /**
     * Sets the game's current phase
     * @param phase
     */
    @Override
    public void setPhase(TurnTracker.Phase phase) {
        assert this.turnTracker != null;

        turnTracker.setPhase(p);
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
        assert playerID >= 0;
        return turnTracker.isPlayersTurn(playerID) && turnTracker.canPlay();
    }

    @Override
    public boolean isTradeActive() {
        return this.currentOffer.isActive();
    }

    @Override
    public int getTradeReceiver() {
        return this.currentOffer.getReceiver();
    }

    @Override
    public int getTradeSender() {
        return this.currentOffer.getSender();
    }

    @Override
    public int getTradeBrick() {
        return this.currentOffer.getBrick();
    }

    @Override
    public int getTradeWood() {
        return this.currentOffer.getWood();
    }

    @Override
    public int getTradeSheep() {
        return this.currentOffer.getSheep();
    }

    @Override
    public int getTradeWheat() {
        return this.currentOffer.getWheat();
    }

    @Override
    public int getTradeOre() {
        return this.currentOffer.getOre();
    }
    //============================================================================================
    //endregion

    //region ResourceBar controller methods
    //======================================================================================
    @Override
    public boolean ableToBuildSettlement(int id) throws PlayerExistsException {
        if(turnTracker.isSetupPhase()){return false;}

        if(getAvailableSettlements(id) == 0){return false;}
        return(playerManager.canBuildSettlement(id) && turnTracker.canPlay() && turnTracker.isPlayersTurn(id));
    }

    @Override
    public boolean ableToBuildRoad(int id) throws PlayerExistsException {
        if(turnTracker.isSetupPhase()){return false;}
        if(getAvailableRoads(id) == 0){return false;}
        return playerManager.canBuildRoad(id) && turnTracker.canPlay() && turnTracker.isPlayersTurn(id);
    }

    @Override
    public boolean ableToBuildCity(int id) throws PlayerExistsException {
        if(getAvailableSettlements(id) == 5){return false;}
        if(turnTracker.isSetupPhase()){return false;}
        if(getAvailableCities(id) == 0){return false;}
        if(turnTracker.isSetupPhase()){return turnTracker.isPlayersTurn(id);}
        return(playerManager.canBuildCity(id) && turnTracker.canPlay() && turnTracker.isPlayersTurn(id));
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

    //region Testing only!!!
    //==============================================
    /**
     * For testing purposes
     * Action - Player rolls the dice
     *
     * @param playerIndex Index of Player performing action
     */
    public int rollNumber(int playerIndex) throws InvalidDiceRollException {
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
    //===============================================
    //endregion




    //Might need
    public getPlayerIdByIndex(int playerIndex){
        return playerManager.getPlayerByIndex(playerIndex).getId();
    }
}
