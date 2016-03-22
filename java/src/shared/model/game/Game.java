package shared.model.game;

import client.data.GameInfo;
import client.data.PlayerInfo;
import com.google.gson.JsonObject;
import server.exceptions.AddAIException;
import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.dto.GameModelDTO;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.JsonSerializable;
import shared.model.ai.AIType;
import shared.model.bank.DevelopmentCardBank;
import shared.model.bank.IResourceCardBank;
import shared.model.bank.InvalidTypeException;
import shared.model.bank.ResourceCardBank;
import shared.model.cards.devcards.DevelopmentCard;
import shared.model.cards.devcards.RoadBuildCard;
import shared.model.cards.devcards.SoldierCard;
import shared.model.cards.resources.*;
import shared.model.game.trade.Trade;
import shared.model.game.trade.TradePackage;
import shared.model.map.Map;
import shared.model.player.Player;
import shared.model.player.PlayerManager;

import javax.naming.InsufficientResourcesException;
import java.util.*;

/**
 * game class representing a Catan game
 */
public class Game extends Observable implements IGame, JsonSerializable {
    //region Member variables
    private int gameId;
    private String title;
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
     * Constructors
     */
    public Game() {
        this.version = -1;
        this.winner = -1;
        this.title = "";
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

    public Game(final String title, final boolean randomPorts, final boolean randomNumbers, final boolean randomTiles) {
        this.version = -1;
        this.winner = -1;
        this.title = title;
        this.map = new Map(randomTiles, randomNumbers, randomPorts);
        this.turnTracker = new TurnTracker();
        this.longestRoadCard = new LongestRoad();
        this.largestArmyCard = new LargestArmy();
        this.playerManager = new PlayerManager(new ArrayList<>());
        this.resourceCardBank = new ResourceCardBank(true);
        this.developmentCardBank = new DevelopmentCardBank(true);
        this.chat = new MessageList();
        this.log = new MessageList();
    }

    public Game(final JsonObject gameJson) {
        assert gameJson != null;
        assert gameJson.has("deck");
        assert gameJson.has("map");
        assert gameJson.has("players");
        assert gameJson.has("bank");
        assert gameJson.has("turnTracker");
        assert gameJson.has("chat");
        assert gameJson.has("log");

        this.developmentCardBank = new DevelopmentCardBank(gameJson.get("deck").getAsJsonObject(), true);
        this.map = new Map(gameJson.get("map").getAsJsonObject());
        this.playerManager = new PlayerManager(gameJson.get("players").getAsJsonArray());
        this.resourceCardBank = new ResourceCardBank(gameJson.get("bank").getAsJsonObject(), true);

        // only update if someone actually has the longest road
        final JsonObject turnTracker = gameJson.getAsJsonObject("turnTracker");
        int longestRoadIndex = turnTracker.get("longestRoad").getAsInt();
        if (longestRoadIndex >= 0) {
            this.longestRoadCard = new LongestRoad(longestRoadIndex);
        }

        this.largestArmyCard = new LargestArmy(turnTracker.get("largestArmy").getAsInt());
        this.version = gameJson.get("version").getAsInt();
        this.winner = gameJson.get("winner").getAsInt();
        if (gameJson.has("tradeOffer")) {
            this.currentOffer = new Trade(gameJson.get("tradeOffer").getAsJsonObject());
        } else {
            this.currentOffer = null;
        }
        try {
            this.turnTracker = new TurnTracker(gameJson.get("turnTracker").getAsJsonObject());
        } catch (BadJsonException e) {
            e.printStackTrace();
        }
        this.chat = new MessageList(gameJson.get("chat").getAsJsonObject());
        this.log = new MessageList(gameJson.get("log").getAsJsonObject());
        this.winner = gameJson.get("winner").getAsInt();
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
     * @param randomPorts
     */
    @Override
    public int initializeGame(final List<Player> players, final boolean randomHexes, final boolean randomChits, final boolean randomPorts) {
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
        assert json.has("version");
        assert json.has("winner");

        this.developmentCardBank = new DevelopmentCardBank(json.get("deck").getAsJsonObject(), true);
        this.map = new Map(json.get("map").getAsJsonObject());
        this.playerManager = new PlayerManager(json.get("players").getAsJsonArray());
        this.resourceCardBank = new ResourceCardBank(json.get("bank").getAsJsonObject(), true);

        // only update if someone actually has the longest road
        final JsonObject turnTracker = json.getAsJsonObject("turnTracker");
        int longestRoadIndex = turnTracker.get("longestRoad").getAsInt();
        if (longestRoadIndex >= 0) {
            this.longestRoadCard = new LongestRoad(longestRoadIndex);
        }

        this.largestArmyCard = new LargestArmy(turnTracker.get("largestArmy").getAsInt());
        this.version = json.get("version").getAsInt();
        this.winner = json.get("winner").getAsInt();
        if (json.has("tradeOffer")) {
            JsonObject offer = json.get("tradeOffer").getAsJsonObject();
            this.currentOffer = new Trade(offer.get("offer").getAsJsonObject());
            this.currentOffer.setReceiver(offer.get("receiver").getAsInt());
            this.currentOffer.setSender(offer.get("playerIndex").getAsInt());
        } else {
            this.currentOffer = null;
        }
        try {
            this.turnTracker = new TurnTracker(json.get("turnTracker").getAsJsonObject());
        } catch (BadJsonException e) {
            e.printStackTrace();
        }
        this.chat = new MessageList(json.get("chat").getAsJsonObject());
        this.log = new MessageList(json.get("log").getAsJsonObject());
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
    public void buildFirstRoad(final int playerIndex, final EdgeLocation location) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert location != null;

        try {
            if (!this.turnTracker.isPlayersTurn(playerIndex)) {
                return;
            }
            if (!this.turnTracker.canPlay()) {
                return;
            }

            this.map.buildRoad(playerIndex, location);
        } catch (InvalidLocationException | StructureException e) {
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
    public void deleteRoad(final int playerIndex, final EdgeLocation edge) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert edge != null;

        try {
            if (!this.turnTracker.isPlayersTurn(playerIndex)) {
                return;
            }
            if (!this.turnTracker.canPlay()) {
                return;
            }

            this.map.deleteRoad(playerIndex, edge);
        } catch (InvalidLocationException | StructureException e) {
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
     * Determines if an AI Player can be added to the game
     *
     * @return
     */
    @Override
    public boolean canAddAI() {
        return playerManager.canAddPlayer();
    }

    /**
     * Determines if a Player can be added to the game
     *
     * @return
     */
    @Override
    public boolean canAddPlayer() {
        return playerManager.canAddPlayer();
    }

    @Override
    public boolean isRejoining(int playerId) {
        return playerManager.isRejoining(playerId);
    }

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
    public boolean canInitiateSettlement(final int playerIndex, final VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canBuildSettlement(final int playerIndex, final VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canBuildCity(final int playerIndex, final VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canInitiateRoad(final int playerIndex, final EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canBuildRoad(final int playerIndex, final EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canDiscardCards(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canRollNumber(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canOfferTrade(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canUseYearOfPlenty(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canUseRoadBuilding(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canPlaceRoadBuildingCard(final int playerID, final EdgeLocation edge) throws InvalidPlayerException, InvalidLocationException, PlayerExistsException {
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
    public boolean canUseSoldier(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canUseMonopoly(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public boolean canUseMonument(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return playerManager.canUseMonument(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
    }

    /**
     * Determine if Player can place the Robber
     * Checks Player turn, event(ie roll 7 or play Soldier)
     *
     * @param playerIndex Index of Player performing action
     * @param location    Where the player wants to place the robber
     * @return True if Player can place the Robber
     */
    @Override
    public boolean canPlaceRobber(final int playerIndex, final HexLocation location) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert location != null;

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
    public boolean canBuyDevelopmentCard(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public boolean canMaritimeTrade(final int playerIndex, final PortType port) throws InvalidPlayerException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert port != null;

        if (canTrade(playerIndex)) {
            Set<PortType> ports = getPortTypes(playerIndex);
            assert ports != null;
            if (ports.contains(port)) {
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
    public boolean canFinishTurn(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return turnTracker.isPlayersTurn(playerIndex);
    }
    //==========================================================================
    //endregion

    //region Do methods
    //==========================================================================

    /**
     * Adds an AI player to the game
     *
     * @param type
     */
    @Override
    public void addAI(AIType type) throws AddAIException {
        if (canAddAI()) {
            //Add the AI
        } else {
            //throw an exception
        }
    }

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
    public void initiateSettlement(int playerIndex, VertexLocation vertex) throws InvalidLocationException, InvalidPlayerException, StructureException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert vertex != null;
        assert this.map != null;

        if (canInitiateSettlement(playerIndex, vertex)) {
            List<ResourceType> resources = map.initiateSettlement(playerIndex, vertex);
            playerManager.getPlayerByIndex(playerIndex).buildFreeSettlement();
            for(ResourceType resource : resources) {
                safeDrawCard(playerIndex, resource);
            }
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
        assert playerIndex < 4;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;
        assert this.map != null;
        assert this.playerManager != null;

        if (canBuildSettlement(playerIndex, vertex)) {
            map.buildSettlement(playerIndex, vertex);
            playerManager.buildSettlement(playerIndex);
            resourceCardBank.addResource(new Brick());
            resourceCardBank.addResource(new Wood());
            resourceCardBank.addResource(new Sheep());
            resourceCardBank.addResource(new Wheat());
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
    public void initiateRoad(int playerIndex, EdgeLocation edge) throws InvalidLocationException, InvalidPlayerException, StructureException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert edge != null;
        assert this.map != null;

        map.initiateRoad(playerIndex, edge);
        playerManager.getPlayerByIndex(playerIndex).buildFreeRoad();
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
        assert playerIndex < 4;
        assert edge != null;
        assert edge.getHexLoc() != null;
        assert edge.getDir() != null;
        assert this.map != null;
        assert this.playerManager != null;
        assert this.longestRoadCard != null;

        if (canBuildRoad(playerIndex, edge)) {
            map.buildRoad(playerIndex, edge);
            playerManager.buildRoad(playerIndex);
            resourceCardBank.addResource(new Brick());
            resourceCardBank.addResource(new Wood());
            updateLongestRoad(playerIndex);
        }
    }

    private void updateLongestRoad(int playerIndex) throws PlayerExistsException {
        //check to update longest road
        int roadLength = map.getLongestRoadSize(playerIndex);
        if (roadLength >= 5 && roadLength > longestRoadCard.getSize()) {
            setPlayerWithLongestRoad(longestRoadCard.getOwner(), playerIndex, roadLength);
        }
    }

    /**
     * Action - Player builds a city
     *
     * @param playerIndex The index of the player that wants to build the city
     * @param vertex      The location of the new city
     * @throws InvalidPlayerException
     * @throws InvalidLocationException
     * @throws StructureException
     * @throws PlayerExistsException
     */
    @Override
    public void buildCity(int playerIndex, VertexLocation vertex) throws InvalidPlayerException, InvalidLocationException, StructureException, PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert vertex != null;
        assert vertex.getDir() != null;
        assert vertex.getHexLoc() != null;

        if (canBuildCity(playerIndex, vertex)) {
            map.buildCity(playerIndex, vertex);
            playerManager.buildCity(playerIndex);
            resourceCardBank.addResource(new Ore());
            resourceCardBank.addResource(new Ore());
            resourceCardBank.addResource(new Ore());
            resourceCardBank.addResource(new Wheat());
            resourceCardBank.addResource(new Wheat());

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
        if (canDiscardCards(playerIndex) && this.turnTracker.canDiscard()) {
            playerManager.discardResourceType(playerIndex, cards);
        }
        List<Player> players = playerManager.getPlayers();
        for(Player player : players) {
            if(!player.hasDiscarded()) {
                return;
            }
        }
        for(Player player : players) {
            player.setDiscarded(false);
        }
        turnTracker.setPhase(TurnTracker.Phase.ROBBING);
    }

    /**
     * Action - Player rolls the dice
     *
     * @param value
     * @throws InvalidDiceRollException
     */
    @Override
    public void rollNumber(final int value) throws Exception {
        //Is value a 7 - robber
        if (value == 7) {
            //Go to discarding phase before robbing if any player has to discard
            List<Player> players = getPlayers();
            for(Player player : players) {
                if (player.canDiscardCards()) {
                    turnTracker.setPhase(TurnTracker.Phase.DISCARDING);
                    playerManager.initializeDiscarding();
                    return;
                }
            }
            //Otherwise just move to the robbing phase
            //Can't move to robbing phase here! need to wait for everyone to discard
        } else {
            //Get the resources
            java.util.Map<Integer, List<ResourceType>> resources = map.getResources(value);

            int bricksNeeded = 0;
            int sheepNeeded = 0;
            int oreNeeded = 0;
            int wheatNeeded = 0;
            int woodNeeded = 0;

            // check for enough resources in game's bank
            for(java.util.Map.Entry<Integer, List<ResourceType>> entry : resources.entrySet()) {
                for(ResourceType type : entry.getValue()) {
                    switch (type) {
                        case BRICK:
                            bricksNeeded++;
                            break;
                        case SHEEP:
                            sheepNeeded++;
                            break;
                        case ORE:
                            oreNeeded++;
                            break;
                        case WHEAT:
                            wheatNeeded++;
                            break;
                        case WOOD:
                            woodNeeded++;
                            break;
                    }
                }
            }

            boolean enoughBrick = resourceCardBank.getNumberOfBrick() >= bricksNeeded;
            boolean enoughSheep = resourceCardBank.getNumberOfSheep() >= sheepNeeded;
            boolean enoughOre = resourceCardBank.getNumberOfOre() >= oreNeeded;
            boolean enoughWheat = resourceCardBank.getNumberOfWheat() >= wheatNeeded;
            boolean enoughWood = resourceCardBank.getNumberOfWood() >= woodNeeded;


            // put available resources in new Hashmap
            // initiate new map to store actual resources to hand out
            HashMap<Integer, List<ResourceType>> resourcesToGive = new HashMap<>();
            for(java.util.Map.Entry<Integer, List<ResourceType>> entry : resources.entrySet()) {
                resourcesToGive.put(entry.getKey(), new ArrayList<ResourceType>());
            }

            for(java.util.Map.Entry<Integer, List<ResourceType>> entry : resources.entrySet()) {
                for (ResourceType type : entry.getValue()) {
                    switch (type) {
                        case BRICK:
                            if (enoughBrick) {
                                resourcesToGive.get(entry.getKey()).add(type);
                            }
                            break;
                        case SHEEP:
                            if (enoughSheep) {
                                resourcesToGive.get(entry.getKey()).add(type);
                            }
                            break;
                        case ORE:
                            if (enoughOre) {
                                resourcesToGive.get(entry.getKey()).add(type);
                            }
                            break;
                        case WHEAT:
                            if (enoughWheat) {
                                resourcesToGive.get(entry.getKey()).add(type);
                            }
                            break;
                        case WOOD:
                            if (enoughWood) {
                                resourcesToGive.get(entry.getKey()).add(type);
                            }
                            break;
                    }
                }
            }

            //Remove from the game's bank and give to players
            for(java.util.Map.Entry<Integer, List<ResourceType>> entry : resourcesToGive.entrySet()) {
                for (ResourceType resource : entry.getValue()) {
                    safeDrawCard(entry.getKey(), resource);
                }
            }
            
            turnTracker.setPhase(TurnTracker.Phase.PLAYING);
        }
    }

    /**
     * Action - Player offers trade
     */
    @Override
    public void offerTrade(TradePackage one, TradePackage two) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        assert one.getPlayerIndex() >= 0;
        assert one.getPlayerIndex() < 4;
        assert two.getPlayerIndex() >= 0;
        assert two.getPlayerIndex() < 4;
        assert one.getPlayerIndex() != two.getPlayerIndex();
        assert one.getResources() != null;
        assert one.getResources().size() > 0;
        assert two.getResources() != null;
        assert two.getResources().size() > 0;
        assert !one.getResources().equals(two.getResources());

        if (canOfferTrade(one.getPlayerIndex())) {
            currentOffer = new Trade(one, two);
            currentOffer.setActive(true);
        }
    }

    public void acceptTrade(int playerIndex, boolean answer) throws PlayerExistsException, InsufficientResourcesException, InvalidTypeException {
        if (playerIndex == currentOffer.getReceiver() && answer) {
            playerManager.offerTrade(currentOffer.getSender(), currentOffer.getReceiver(), currentOffer.getPackage1().getResources(), currentOffer.getPackage2().getResources());
        }
        currentOffer = null;
    }

    /**
     * Action - Player plays Year of Plenty
     *
     * @param playerIndex index of Player performing action
     * @param want1       The first resource the player wants to receive with Year of Plenty
     * @param want2       the second resource the player wants to receive with Year of Plenty
     */
    @Override
    public void useYearOfPlenty(final int playerIndex, final ResourceType want1, final ResourceType want2) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert want1 != null;
        assert want2 != null;

        if (canUseYearOfPlenty(playerIndex)) {
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
    public void useRoadBuilder(final int playerIndex, final EdgeLocation edge1, final EdgeLocation edge2) throws PlayerExistsException, DevCardException, InvalidPlayerException, InvalidLocationException, StructureException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert edge1 != null;
        assert edge1.getHexLoc() != null;
        assert edge2 != null;
        assert edge2.getHexLoc() != null;
        assert !edge1.equals(edge2);

        if (canUseRoadBuilding(playerIndex)) {
            map.buildRoad(playerIndex, edge1);
            map.buildRoad(playerIndex, edge2);
            playerManager.useRoadBuilder(playerIndex);
            updateLongestRoad(playerIndex);
        }
    }

    /**
     * Cancels playing the road builder
     *
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
    public void useSoldier(int playerIndex, int victimIndex, HexLocation location) throws MoveRobberException, InvalidTypeException, InsufficientResourcesException, PlayerExistsException, DevCardException, AlreadyRobbedException, InvalidLocationException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert victimIndex >= 0;
        assert victimIndex < 4;
        assert this.playerManager != null;
        assert this.largestArmyCard != null;
        assert this.turnTracker != null;

        if (canUseSoldier(playerIndex)) {
            playerManager.useSoldier(playerIndex);
            int used = playerManager.getNumberOfSoldiers(playerIndex);
            if (used >= 3 && used > largestArmyCard.getMostSoldiers()) {
                final int oldPlayer = largestArmyCard.getOwner();
                largestArmyCard.setNewOwner(playerIndex, used);
                playerManager.changeLargestArmyPossession(oldPlayer, playerIndex);
            }
            if (canPlaceRobber(playerIndex, location)) {
                rob(playerIndex, victimIndex, location);
            }
        }
    }

    /**
     * Cancels playing a soldier card
     *
     * @param playerIndex
     */
    public void cancelSoldierCard(final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            SoldierCard sc = new SoldierCard();
            this.playerManager.addDevCard(playerIndex, sc);
        } catch (PlayerExistsException e) {
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
    public void useMonopoly(final int playerIndex, final ResourceType type) throws PlayerExistsException, DevCardException, InsufficientResourcesException, InvalidTypeException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert type != null;

        if (canUseMonopoly(playerIndex)) {
            playerManager.useMonopoly(playerIndex, type);
        }
    }

    /**
     * Action - Player plays Monument
     *
     * @param playerIndex Index of Player performing action
     */
    @Override
    public void useMonument(final int playerIndex) throws PlayerExistsException, DevCardException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        if (canUseMonument(playerIndex)) {
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
    public Set<Integer> placeRobber(final int playerIndex, final HexLocation location) throws AlreadyRobbedException, InvalidLocationException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public void rob(int playerRobber, int playerRobbed, HexLocation hexLoc) throws AlreadyRobbedException, InvalidLocationException, MoveRobberException, InvalidTypeException, PlayerExistsException, InsufficientResourcesException {
        assert playerRobbed >= 0;
        assert playerRobber >= 0;
        assert hexLoc != null;
        assert this.map != null;
        assert this.turnTracker != null;
        assert this.playerManager != null;

        if(playerRobber == playerRobbed) {
            placeRobber(playerRobber, hexLoc);
            turnTracker.setPhase(TurnTracker.Phase.PLAYING);
            return;
        }

        final Set<Integer> who = map.whoCanGetRobbed(playerRobber, hexLoc);
        assert who != null;
        if (turnTracker.isPlayersTurn(playerRobber) && turnTracker.canUseRobber() && who.contains(playerRobbed)) {
            map.moveRobber(playerRobber, hexLoc);
            try {
                playerManager.placeRobber(playerRobber, playerRobbed);
                turnTracker.setPhase(TurnTracker.Phase.PLAYING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //turnTracker.setPhase(TurnTracker.Phase.PLAYING); for Joel, love Corbin
    }

    /**
     * Action - Player buys a new developmentCard
     * deducts cards
     * adds new developmentCard to his DCBank
     *
     * @param playerIndex
     */
    @Override
    public void buyDevelopmentCard(int playerIndex) throws Exception {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert this.playerManager != null;
        assert this.developmentCardBank != null;

        // remove player resources
        playerManager.buyDevCard(playerIndex);

        resourceCardBank.addResource(new Ore());
        resourceCardBank.addResource(new Wheat());
        resourceCardBank.addResource(new Sheep());

        // give Dev Card from game to player
        final DevelopmentCard dc = developmentCardBank.draw();
        playerManager.addDevCard(playerIndex, dc);
    }

    /**
     * Action - Player performs a maritime trade
     *
     * @param playerIndex
     */
    @Override
    public void maritimeTrade(final int playerIndex, final int ratio, final ResourceType send, final ResourceType receive) throws InvalidPlayerException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert send != null;
        assert receive != null;

        final List<ResourceType> cards = new ArrayList<>();
        if (ratio == 3 && !canMaritimeTrade(playerIndex, PortType.THREE)) {
            return;
        } else if (ratio == 2) {
            switch (send) {
                case BRICK:
                    if (!canMaritimeTrade(playerIndex, PortType.BRICK)) {
                        return;
                    }
                    break;
                case ORE:
                    if (!canMaritimeTrade(playerIndex, PortType.ORE)) {
                        return;
                    }
                    break;
                case SHEEP:
                    if (!canMaritimeTrade(playerIndex, PortType.SHEEP)) {
                        return;
                    }
                    break;
                case WHEAT:
                    if (!canMaritimeTrade(playerIndex, PortType.WHEAT)) {
                        return;
                    }
                    break;
                case WOOD:
                    if (!canMaritimeTrade(playerIndex, PortType.WOOD)) {
                        return;
                    }
                    break;
            }
        }
        for (int i = 0; i < ratio; i++) {
            cards.add(send);
        }
        final List<ResourceCard> discarded = playerManager.discardResourceType(playerIndex, cards);
        assert discarded != null;
        for (ResourceCard rc : discarded) {
            resourceCardBank.addResource(rc);
        }

        playerManager.addResource(playerIndex, resourceCardBank.discard(receive));

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
            playerManager.finishTurn(playerIndex);
        } catch (BadCallerException e) {
            e.printStackTrace();
        }
        return turnTracker.nextTurn();

    }
    //==========================================================================
    //endregion

    //region Getters
    //==========================================================================

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
     * @return An object-representation of the Catan Map
     */
    @Override
    public Map getMap() {
        return this.map;
    }

    /**
     * Gets the chat
     *
     * @return A MessageList object that contains the chat
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
     * @param playerIndex The index of the player who's ports we are getting
     * @return
     * @throws InvalidPlayerException
     */
    @Override
    public Set<PortType> getPortTypes(final int playerIndex) throws InvalidPlayerException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
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
    public Integer getAvailableRoads(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return playerManager.getAvailableRoads(playerIndex);
    }

    /**
     * Get the number of settlements the player has left
     *
     * @param playerIndex The index of the player who's settlement count we are getting
     * @return The number of settlements owned by the player
     * @throws PlayerExistsException
     */
    @Override
    public Integer getAvailableSettlements(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public Integer getAvailableCities(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public Player getPlayerById(final int id) throws PlayerExistsException {
        assert id >= 0;

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
        final HashMap<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK, this.resourceCardBank.getNumberOfBrick());
        resources.put(ResourceType.WOOD, this.resourceCardBank.getNumberOfWood());
        resources.put(ResourceType.ORE, this.resourceCardBank.getNumberOfOre());
        resources.put(ResourceType.WHEAT, this.resourceCardBank.getNumberOfWheat());
        resources.put(ResourceType.SHEEP, this.resourceCardBank.getNumberOfSheep());
        return resources;
    }

    /**
     * Get the player's resources
     *
     * @param playerIndex The index of the player whose resources we are getting.
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public HashMap<ResourceType, Integer> getPlayerResources(int playerIndex) throws PlayerExistsException {
        final HashMap<ResourceType, Integer> resources = new HashMap<>();
        final IResourceCardBank resourceCardBank = this.playerManager.getPlayerByIndex(playerIndex).getResourceCardBank();
        resources.put(ResourceType.BRICK, resourceCardBank.getNumberOfBrick());
        resources.put(ResourceType.WOOD, resourceCardBank.getNumberOfWood());
        resources.put(ResourceType.ORE, resourceCardBank.getNumberOfOre());
        resources.put(ResourceType.WHEAT, resourceCardBank.getNumberOfWheat());
        resources.put(ResourceType.SHEEP, resourceCardBank.getNumberOfSheep());
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
    public int getPoints(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final Player player = playerManager.getPlayerByIndex(playerIndex);
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
    public Integer numberOfDevCard(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert this.playerManager != null;

        return playerManager.getPlayerByIndex(playerIndex).quantityOfDevCards();
    }

    /**
     * Get the number of development cards (specific type) the player has
     *
     * @param type
     * @param playerIndex
     * @return
     */
    @Override
    public int getNumberDevCards(final DevCardType type, final int playerIndex) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert type != null;

        return playerManager.getNumberDevCards(type, playerIndex);
    }

    /**
     * Get the number of resource cards the player has
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public int getNumberResourceCards(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return playerManager.getNumberResourceCards(playerIndex);
    }

    /**
     * Get the number of resources cards (specific type) the player has
     *
     * @param playerIndex
     * @param t
     * @return
     * @throws PlayerExistsException
     * @throws InvalidTypeException
     */
    @Override
    public int amountOwnedResource(final int playerIndex, final ResourceType t) throws PlayerExistsException, InvalidTypeException {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert t != null;

        return playerManager.getPlayerByIndex(playerIndex).howManyOfThisCard(t);
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
     *
     * @param playerIndex
     * @return
     */
    @Override
    public int getPlayerIdByIndex(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

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
    public String getPlayerNameByIndex(final int playerIndex) throws PlayerExistsException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        return playerManager.getPlayerByIndex(playerIndex).getName();
    }

    /**
     * Get the player's color based on their name
     *
     * @param player
     * @return
     */
    @Override
    public CatanColor getPlayerColorByName(final String player) {
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
     *
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
     *
     * @param phase
     */
    @Override
    public void setPhase(TurnTracker.Phase phase) {
        assert this.turnTracker != null;

        turnTracker.setPhase(phase);
    }
    //==========================================================================
    //endregion

    //region Domestic trade methods
    //==========================================================================

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
        if(currentOffer == null){
        }else{
            return true;
        }
        return (currentOffer != null);
    }

    @Override
    public int getTradeReceiver() {
        assert currentOffer != null;
        return this.currentOffer.getReceiver();
    }

    @Override
    public int getTradeSender() {
        assert currentOffer != null;
        return this.currentOffer.getSender();
    }

    @Override
    public int getTradeBrick() {
        assert currentOffer != null;
        return this.currentOffer.getBrick();
    }

    @Override
    public int getTradeWood() {
        assert currentOffer != null;
        return this.currentOffer.getWood();
    }

    @Override
    public int getTradeSheep() {
        assert currentOffer != null;
        return this.currentOffer.getSheep();
    }

    @Override
    public int getTradeWheat() {
        assert currentOffer != null;
        return this.currentOffer.getWheat();
    }

    @Override
    public int getTradeOre() {
        assert currentOffer != null;
        return this.currentOffer.getOre();
    }
    //==========================================================================
    //endregion

    //region ResourceBar controller methods
    //==========================================================================

    /**
     * Determine if the player can build a settlement
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildSettlement(int playerIndex) throws PlayerExistsException {
        if (turnTracker.isSetupPhase()) {
            return false;
        }

        return getAvailableSettlements(playerIndex) != 0 && (playerManager.canBuildSettlement(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex));
    }

    /**
     * Determine if the player can build a road
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildRoad(int playerIndex) throws PlayerExistsException {
        if (turnTracker.isSetupPhase()) {
            return false;
        }
        return getAvailableRoads(playerIndex) != 0 && playerManager.canBuildRoad(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex);
    }

    /**
     * Determine if the player can build a city
     *
     * @param playerIndex
     * @return
     * @throws PlayerExistsException
     */
    @Override
    public boolean ableToBuildCity(int playerIndex) throws PlayerExistsException {
        if (getAvailableSettlements(playerIndex) == 5) {
            return false;
        }
        if (turnTracker.isSetupPhase()) {
            return false;
        }
        if (getAvailableCities(playerIndex) == 0) {
            return false;
        }
        if (turnTracker.isSetupPhase()) {
            return turnTracker.isPlayersTurn(playerIndex);
        }

        return (playerManager.canBuildCity(playerIndex) && turnTracker.canPlay() && turnTracker.isPlayersTurn(playerIndex));
    }
    //==========================================================================
    //endregion

    //region Serialization
    //==========================================================================

    /**
     * Converts the object to JSON
     *
     * @return The JSON representation of the object
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.add("deck", developmentCardBank.toJSON());
        json.add("bank", resourceCardBank.toJSON());
        json.add("chat", chat.toJSON());
        json.add("log", log.toJSON());
        json.add("map", map.toJSON());
        json.add("players", playerManager.toJSON());
        if(currentOffer != null) {
            json.add("tradeOffer", currentOffer.toJSON());
        }

        JsonObject turn = turnTracker.toJSON();
        turn.addProperty("longestRoad", longestRoadCard.getOwner());
        turn.addProperty("largestArmy", largestArmyCard.getOwner());
        json.add("turnTracker", turn);


        json.addProperty("version", version);
        json.addProperty("winner", winner);

        return json;
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
    public int rollDice(final int playerIndex) throws InvalidDiceRollException {
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final Dice dice = new Dice(2);
        final int roll = dice.roll();
        if (roll == 7) {
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
     * TODO - this is dumb - shouldn't be here
     *
     * @param card
     * @param index
     * @throws PlayerExistsException
     */
    public void giveResource(final ResourceCard card, final int index) throws PlayerExistsException {
        assert card != null;
        assert index >= 0;
        assert index < 4;

        playerManager.addResource(index, card);
    }

    /**
     * for testing purposes.
     *
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
     * Safely tries to draw a card from the bank and give to the player
     *
     * @param playerIndex
     * @param type
     */
    private void safeDrawCard(final int playerIndex, final ResourceType type) {
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert type != null;

        try {
            ResourceCard card = resourceCardBank.draw(type);
            playerManager.addResource(playerIndex, card);
        } catch (InvalidTypeException | Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the dev card to the player
     *
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
     * deducts Victory Points from oldOwnerIndex
     * adds Victory Points to newOwnerIndex
     * Updates LongestRoad for newOwnerIndex and roadSize
     *
     * @param oldOwnerIndex
     * @param newOwnerIndex
     * @param roadSize
     */
    private void setPlayerWithLongestRoad(final int oldOwnerIndex, final int newOwnerIndex, final int roadSize) throws PlayerExistsException {
        assert newOwnerIndex >= 0;
        assert newOwnerIndex < 4;
        assert oldOwnerIndex < 4;
        assert roadSize >= 0;

        playerManager.changeLongestRoadPossession(oldOwnerIndex, newOwnerIndex);
        longestRoadCard.setOwner(newOwnerIndex, roadSize);
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public List<PlayerInfo> getPlayerInfos() {
        List<PlayerInfo> infos = new ArrayList<>();
        for (final Player p : this.getPlayers()) {
            // TODO - fix last two booleans to actual values
            final PlayerInfo tmpPlayerInfo = new PlayerInfo(p.getName(), p.getVictoryPoints(), p.getColor(), p.getId(), p.getPlayerIndex(), false, false);
            infos.add(tmpPlayerInfo);
        }
        return infos;
    }

    public GameInfo getAsGameInfo() {
        final GameInfo gameInfo = new GameInfo();
        gameInfo.setId(this.getId());
        gameInfo.setTitle(this.title);
//        gameInfo.setPlayers(this.playerManager.); TODO
        return gameInfo;
    }

    public GameModelDTO getDTO() {
        return new GameModelDTO(toJSON());
    }

    public void incrementVersion() {
        this.version++;
    }

    public void log(String name, String message) {
        assert name != null;
        assert message != null;

        this.log.addMessage(new MessageLine(name, message));
    }

}
