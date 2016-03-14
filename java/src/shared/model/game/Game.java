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
     * @param randomHexes
     * @param randomChits
     * @param randomPorts @return
     */
    @Override
    public int initializeGame(List<Player> players, boolean randomHexes, boolean randomChits, boolean randomPorts) {
        assert players != null;
        assert this.playerManager != null;
        assert this.map != null;
        assert this.turnTracker != null;

        //Add players to PlayerManager
        this.playerManager = new PlayerManager(players);
        this.map = new Map(randomHexes, randomChits, randomPorts);
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
            if(!this.turnTracker.isPlayersTurn(playerIndex)){return;}
            if(!this.turnTracker.canPlay()){return;}

            this.map.buildRoad(playerIndex, location);
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
            if(!this.turnTracker.isPlayersTurn(playerIndex)){return;}
            if(!this.turnTracker.canPlay()){return;}

            this.map.deleteRoad(playerIndex, edge);
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
     * @param location
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(int playerIndex, HexLocation location) {
        assert playerIndex >= 0;

        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.canUseRobber() && map.canMoveRobber(location);
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

        if(canTrade(playerIndex)){
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert this.map != null;

        if(canInitiateSettlement(playerIndex, vertex)){
            map.initiateSettlement(playerIndex, vertex);
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert this.map != null;
        assert this.playerManager != null;

        if(canBuildSettlement(playerIndex, vertex)) {
            map.buildSettlement(playerIndex, vertex);
            playerManager.buildSettlement(playerIndex);
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
        assert playerIndex >= 0;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getDir() != null;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.longestRoadCard != null;

        if(canBuildRoad(playerIndex, edge)) {
            map.buildRoad(playerIndex, edge);
            playerManager.buildRoad(playerIndex);
            //check to update longest road
            int roadlength = map.getLongestRoadSize(playerIndex);
            if(roadlength >= 5 && roadlength > longestRoadCard.getSize()){
                setPlayerWithLongestRoad(longestRoadCard.getOwner(), playerIndex, roadlength);
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
        assert playerIndex >= 0;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;

        if(canBuildCity(playerIndex, vertex)) {
            map.buildCity(playerIndex, vertex);
            playerManager.buildCity(playerIndex);
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
            final TradePackage two = new TradePackage(playerIndexTwo, playerTwoCards);

            // TODO - why is this trade object unused?
            Trade trade = new Trade(one,two);

            playerManager.offerTrade(playerIndexOne,playerIndexTwo,playerOneCards,playerTwoCards); //// TODO: 2/15/16 poorly named function.  OfferTrade shouldn't do the trade.

        }
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerIndex index of Player performing action
     * @param want1
     * @param want2
     */
    @Override
    public void useYearOfPlenty(int playerIndex, ResourceType want1, ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert want1 != null;
        assert want2 != null;

        if(canUseYearOfPlenty(playerIndex)) {
            playerManager.useYearOfPlenty(playerIndex);
            ResourceCard rc1 = resourceCardBank.discard(want1);
            ResourceCard rc2 = resourceCardBank.discard(want2);
            playerManager.addResource(playerIndex, rc1);
            playerManager.addResource(playerIndex, rc2);
        }
    }

    /**
     * Action - Player plays Road Builder
     *
     * @param playerIndex index of Player performing action
     * @param edge1
     * @param edge2
     */
    @Override
    public void useRoadBuilder(int playerIndex, EdgeLocation edge1, EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException {
        assert playerIndex >= 0;
        assert edge1 != null;
        assert edge1.getHexLoc() != null;
        assert edge2 != null;
        assert edge2.getHexLoc() != null;
        assert !edge1.equals(edge2);

        if(canUseRoadBuilding(playerIndex)) {
            playerManager.useRoadBuilder(playerIndex);
            buildRoad(playerIndex, edge1);
            buildRoad(playerIndex, edge2);
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
     * @param playerIndex ID of Player performing action
     * @param location
     */
    @Override
    public Set<Integer> useSoldier(int playerIndex, HexLocation location) throws PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException {
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
            if(canPlaceRobber(playerIndex, location)) {
                return placeRobber(playerIndex, location);
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
            this.playerManager.addDevCard(playerIndex, sc);
        } catch(PlayerExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Action - Player plays Monopoly
     *
     * @param playerIndex Index of Player performing action
     * @param type
     */
    @Override
    public void useMonopoly(int playerIndex, ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert type != null;

        if(canUseMonopoly(playerIndex)) {
            playerManager.useMonopoly(playerIndex, type);
        }
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public void useMonument(int playerIndex) throws PlayerExistsException, DevCardException {
        assert playerIndex >= 0;

        if(canUseMonument(playerIndex)){
            playerManager.useMonument(playerIndex);
        }
    }

    /**
     * Action - Player places the Robber
     *
     * @param playerIndex ID of Player performing action
     * @param location
     */
    @Override
    public Set<Integer> placeRobber(int playerIndex, HexLocation location) throws AlreadyRobbedException, InvalidLocationException {
        assert playerIndex >= 0;
        assert location != null;

        return map.moveRobber(playerIndex, location);
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
        assert playerIndex >= 0;
        assert this.playerManager != null;
        assert this.developmentCardBank != null;

        playerManager.buyDevCard(playerIndex);
        final DevelopmentCard dc = developmentCardBank.draw();
        playerManager.addDevCard(playerIndex, dc);
        return dc.getType();
    }

    //TODO: Maritime messed up
    /**
     * Action - Player performs a maritime trade
     *
     * @param playerIndex
     * @param port
     * @param want
     */
    @Override
    public void maritimeTrade(int playerIndex, PortType port, ResourceType want) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerIndex >= 0;
        assert port != null;
        assert want != null;

        if(canMaritimeTrade(playerIndex, port)){
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
            final List<ResourceCard> discarded = playerManager.discardResourceType(playerIndex, cards);
            assert discarded != null;
            for(ResourceCard rc: discarded) {
                resourceCardBank.addResource(rc);
            }

            playerManager.addResource(playerIndex, resourceCardBank.discard(want));
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
        assert playerIndex >= 0;
        assert this.map != null;

        return map.getPortTypes(playerIndex);
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
        return playerManager.getAvailableRoads(playerIndex);
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
        return playerManager.getAvailableSettlements(playerIndex);
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
        return playerManager.getAvailableCities(playerIndex);
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
        resources.put(ResourceType.BRICK,this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank().getNumberOfBrick());
        resources.put(ResourceType.WOOD,this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank().getNumberOfWood());
        resources.put(ResourceType.ORE,this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank().getNumberOfOre());
        resources.put(ResourceType.WHEAT,this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank().getNumberOfWheat());
        resources.put(ResourceType.SHEEP,this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank().getNumberOfSheep());
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
        assert playerIndex >= 0;
        assert this.playerManager != null;

        return playerManager.getPlayerByIndex(playerIndex).quantityOfDevCards();
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
     * Get the id of the player associated with the given index
     * @param playerIndex
     * @return
     */
    @Override
    public int getPlayerIdByIndex(int playerIndex) throws PlayerExistsException{
        try {
            return playerManager.getPlayerByIndex(playerIndex).getId();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
        }

        throw new PlayerExistsException("The player at index " + playerIndex + " doesn't exist.");
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

        turnTracker.setPhase(phase);
    }
    //===================================================================================================
    //endregion

    //region Domestic trade methods
    //============================================================================
    /**
     * checks if the player is in the trade sequence of his turn
     *
     * @param playerIndex
     * @return
     */
    @Override
    public boolean canTrade(int playerIndex) {
        assert playerIndex >= 0;
        return turnTracker.isPlayersTurn(playerIndex) && turnTracker.canPlay();
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
    /**
     * Determine if the player can build a settlement
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildSettlement(int playerIndex) throws PlayerExistsException {
        if(turnTracker.isSetupPhase()){return false;}

        if(getAvailableSettlements(playerIndex) == 0){return false;}
        return(playerManager.canBuildSettlement(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex));
    }

    /**
     * Determine if the player can build a road
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildRoad(int playerIndex) throws PlayerExistsException {
        if(turnTracker.isSetupPhase()){return false;}
        if(getAvailableRoads(playerIndex) == 0){return false;}
        return playerManager.canBuildRoad(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
    }

    /**
     * Determine if the player can build a city
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildCity(int playerIndex) throws PlayerExistsException {
        if(getAvailableSettlements(playerIndex) == 5){return false;}
        if(turnTracker.isSetupPhase()){return false;}
        if(getAvailableCities(playerIndex) == 0){return false;}
        if(turnTracker.isSetupPhase()){return turnTracker.isPlayersTurn(playerIndex);}
        return(playerManager.canBuildCity(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex));
    }
    //=========================================================================================
    //endregion

    //region Serialization
    //=====================================================================
    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        return null;
    }
    //=====================================================================
    //endregion

    //region Testing only!!!
    //==============================================
    /**
     * For testing purposes
     * Action - Player rolls the dice
     *
     * @param playerIndex Index of Player performing action
     */
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

    //region Helpers
    //==========================================================
    /**
     * Adds the dev card to the player
     * @param dc
     * @param playerIndex
     * @throws PlayerExistsException
     */
    public void addDevCard(final DevelopmentCard dc, final int playerIndex) throws PlayerExistsException {
        assert dc != null;
        assert playerIndex >= 0;
        assert this.playerManager != null;

        playerManager.getPlayerByIndex(playerIndex).addDevCard(dc);
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
    private void setPlayerWithLongestRoad(int playerIDOld, int playerIDNew, int roadSize) {
        assert playerIDNew >= 0;
        assert playerIDOld >= 0;
        assert roadSize >= 0;
        assert playerIDNew != playerIDOld;

        longestRoadCard.setOwner(playerIDNew, roadSize);
    }
    //==========================================================
    //endregion
}
