package server.facade;

import com.google.gson.JsonObject;
import server.exceptions.*;
import server.managers.GameManager;
import server.managers.UserManager;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.GameModelDTO;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.cards.resources.ResourceCard;
import shared.model.game.Game;

import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 3/10/2016.
 */
public class ServerFacade implements IFacade {
    private static IFacade _instance;
    private GameManager gameManager;
    private UserManager userManager;

    /**
     * Default Constructor - Private
     */
    private ServerFacade(){
        gameManager = new GameManager();
        userManager = new UserManager();
    }

    /**
     * Singleton - get instance method
     * @return
     */
    public static IFacade getInstance(){
        if(_instance == null) {
            _instance = new ServerFacade();
        }
        return _instance;
    }

    /**
     * Logs a player into the server
     *
     * @param username
     * @param password
     * @throws LoginException
     */
    @Override
    public void login(String username, String password) throws LoginException {

    }

    /**
     * Registers a user
     *
     * @param username
     * @param password
     * @throws RegisterException
     */
    @Override
    public void register(String username, String password) throws RegisterException {

    }

    /**
     * Adds an AI to the game
     *
     * @param aiType
     * @throws AddAIException
     */
    @Override
    public void addAI(int gameID, Object aiType) throws AddAIException {

    }

    /**
     * List the available AI types
     *
     * @throws ListAIException
     */
    @Override
    public void listAI() throws ListAIException {

    }

    /**
     * List the current games
     *
     * @return info on the current games
     * @throws ListException
     */
    @Override
    public JsonObject list() throws ListException {
        return null;
    }

    /**
     * Creates a new game
     *
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     * @return
     * @throws CreateGameException
     */
    @Override
    public JsonObject create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) throws CreateGameException {
        return null;
    }

    /**
     * Joins a player to the specified game
     *
     * @param gameID
     * @param color
     * @throws JoinGameException
     */
    @Override
    public void join(int gameID, CatanColor color) throws JoinGameException {

    }

    /**
     * Sends a chat message
     *
     * @param player  index of the player
     * @param message
     * @throws SendChatException
     */
    @Override
    public void sendChat(int gameID, int player, String message) throws SendChatException {

    }

    /**
     * Rolls the specified value
     *
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     */
    @Override
    public void rollNumber(int gameID, int player, int value) throws RollNumberException {

    }

    /**
     * Robs the specified player
     *
     * @param player      index of the player robbing
     * @param newLocation
     * @param victim      index of the player being robbed
     * @return GameModelDTO
     * @throws RobPlayerException
     */
    @Override
    public GameModelDTO robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException {
        assert gameID >= 0;
        assert player >= 0;
        assert newLocation != null;
        assert victim >= 0;

        Game game = gameManager.getGameByID(gameID);
        try {
            //TODO: fix model robbing cuz its a mess
        } catch (Exception e) {
            throw new RobPlayerException(e.getMessage());
        }
    }

    /**
     * Ends the current player's turn making it the next player's turn
     *
     * @param player index of the player
     * @throws FinishTurnException
     */
    @Override
    public void finishTurn(int gameID, int player) throws FinishTurnException {

    }

    /**
     * Buys a new dev card
     *
     * @param player index of the player
     * @throws BuyDevCardException
     */
    @Override
    public void buyDevCard(int gameID, int player) throws BuyDevCardException {

    }

    /**
     * Handles playing Year of Plenty
     *
     * @param player      index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     */
    @Override
    public void yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException {

    }

    /**
     * Handles playing Road Building
     *
     * @param player      index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @return GameModelDTO
     * @throws RoadBuildingException
     */
    @Override
    public GameModelDTO roadBuilding(int gameID, int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException {
        assert gameID >= 0;
        assert player >= 0;
        assert locationOne != null;
        assert locationTwo != null;

        Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canUseRoadBuilding(player)) {
                game.useRoadBuilder(player, locationOne, locationTwo);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException | DevCardException e) {
            throw new RoadBuildingException(e.getMessage());
        }
    }

    /**
     * Handles playing Soldier
     *
     * @param player index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @return GameModelDTO
     * @throws SoldierException
     */
    @Override
    public GameModelDTO soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException {

    }

    /**
     * Hanldes playing Monopoly
     *
     * @param player   index of the player
     * @param resource resource to take
     * @throws MonopolyException
     */
    @Override
    public void monopoly(int gameID, int player, ResourceType resource) throws MonopolyException {

    }

    /**
     * Handles playing Monument
     *
     * @param player index of the player
     * @throws MonumentException
     */
    @Override
    public void monument(int gameID, int player) throws MonumentException {

    }

    /**
     * Builds a road
     *
     * @param player   index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildRoadException
     */
    @Override
    public GameModelDTO buildRoad(int gameID, int player, EdgeLocation location) throws BuildRoadException {
        assert gameID >= 0;
        assert player >= 0;
        assert location != null;

        Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canInitiateRoad(player, location)) {
                game.initiateRoad(player, location);
            } else if(game.canBuildRoad(player, location)) {
                game.buildRoad(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildRoadException(e.getMessage());
        }
    }

    /**
     * Builds a settlement
     *
     * @param player index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildSettlementException
     */
    @Override
    public GameModelDTO buildSettlement(int gameID, int player, VertexLocation location) throws BuildSettlementException {
        assert gameID >= 0;
        assert player >= 0;
        assert location != null;

        Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canInitiateSettlement(player, location)) {
                game.initiateSettlement(player, location);
            } else if(game.canBuildSettlement(player, location)) {
                game.buildSettlement(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildSettlementException(e.getMessage());
        }
    }

    /**
     * Builds a city
     *
     * @param gameID id of the game
     * @param player index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildCityException
     */
    @Override
    public GameModelDTO buildCity(int gameID, int player, VertexLocation location) throws BuildCityException {
        assert gameID >= 0;
        assert player >= 0;
        assert location != null;

        Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canBuildCity(player, location)) {
                game.buildCity(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildCityException(e.getMessage());
        }
    }

    /**
     * Offers a trade to the specified player
     *
     * @param player
     * @param recipient
     * @param send
     * @param receive
     * @throws OfferTradeException
     */
    @Override
    public void offerTrade(int gameID, int player, int recipient, List<ResourceType> send, List<ResourceType> receive) throws OfferTradeException {

    }

    /**
     * Accepts a trade offer
     *
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     */
    @Override
    public void acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException {

    }

    /**
     * Performs a maritime trade (trade with the bank)
     *
     * @param player index of the player
     * @param ratio  trade ratio [2, 3 or 4]
     * @param give   resource to trade away
     * @param get    resource to get
     * @throws MaritimeTradeException
     */
    @Override
    public void maritimeTrade(int gameID, int player, int ratio, ResourceType give, ResourceType get) throws MaritimeTradeException {

    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @param player         index of the player discarding
     * @param cardsToDiscard list of cards to be discarded
     * @throws DiscardCardsException
     */
    @Override
    public void discardCards(int gameID, int player, List<ResourceCard> cardsToDiscard) throws DiscardCardsException {

    }
}
