package server.facade;

import com.google.gson.JsonObject;
import server.exceptions.*;
import server.managers.GameManager;
import server.managers.UserManager;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.*;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ai.AIType;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;
import shared.model.game.trade.Trade;
import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
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
     * @param type
     * @throws AddAIException
     */
    @Override
    public GameModelDTO addAI(int gameId, AIType type) throws AddAIException {
        Game game = gameManager.getGameByID(gameId);

        if(game.canAddAI()){
            game.addAI(type);
        }else{
            throw new AddAIException("AI player can't be added!");
        }

        return game.getDTO();
    }

    /**
     * List the available AI types
     *
     * @throws ListAIException
     */
    @Override
    public ListAIDTO listAI(int gameId) throws ListAIException {
        Game game = gameManager.getGameByID(gameId);
        return new ListAIDTO(game.getAITypes());
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
            if(game.canPlaceRobber(player, newLocation)) {
                game.rob(player, victim, newLocation);
                return game.getDTO();
            }
        } catch (InvalidTypeException | InsufficientResourcesException | MoveRobberException | AlreadyRobbedException | PlayerExistsException | InvalidLocationException e) {
            throw new RobPlayerException(e.getMessage());
        }
        return null;
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
     * @param playerIndex index of the player
     * @throws BuyDevCardException
     */
    @Override
    public GameModelDTO buyDevCard(int gameID, int playerIndex) throws BuyDevCardException {
        Game game = gameManager.getGameByID(gameID);
        try {
            game.buyDevelopmentCard(playerIndex);
        } catch (Exception e) {
            throw new BuyDevCardException("Something went wrong while trying to buy a dev card");
        }
        return game.getDTO();
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
                return game.getDTO();
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException | DevCardException e) {
            throw new RoadBuildingException(e.getMessage());
        }
        return null;
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
        assert gameID >= 0;
        assert player >= 0;
        assert newLocation != null;
        assert victim >= 0;

        Game game = gameManager.getGameByID(gameID);
        try{
            if(game.canUseSoldier(player)) {
                game.useSoldier(player, victim, newLocation);
                return game.getDTO();
            }
        } catch(MoveRobberException | InvalidTypeException | InsufficientResourcesException | DevCardException | PlayerExistsException | AlreadyRobbedException | InvalidLocationException e) {
            throw new SoldierException(e.getMessage());
        }
        return null;
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
                return game.getDTO();
            } else if(game.canBuildRoad(player, location)) {
                game.buildRoad(player, location);
                return game.getDTO();
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildRoadException(e.getMessage());
        }
        return null;
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
                return game.getDTO();
            } else if(game.canBuildSettlement(player, location)) {
                game.buildSettlement(player, location);
                return game.getDTO();
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildSettlementException(e.getMessage());
        }
        return null;
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
                return game.getDTO();
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildCityException(e.getMessage());
        }
        return null;
    }

    /**
     * Offers a trade to the specified player
     *
     * @throws OfferTradeException
     */
    @Override
    public GameModelDTO offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException {
        int sender = dto.getSender();
        int receiver = dto.getReceiver();
        Trade offer = dto.getOffer();
        List<ResourceType> send = offer.getPackage1().getResources();
        List<ResourceType> receive = offer.getPackage2().getResources();
        try {
            gameManager.getGameByID(gameID).offerTrade(sender, receiver, send, receive);
            return gameManager.getGameByID(gameID).getDTO();
        } catch(InvalidTypeException | PlayerExistsException | InsufficientResourcesException e){
            throw new OfferTradeException(e.getMessage());
        }
    }

    /**
     * Accepts a trade offer
     *
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     */
    @Override
    public GameModelDTO acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException {
        try {
            gameManager.getGameByID(gameID).acceptTrade(player,willAccept);
            return gameManager.getGameByID(gameID).getDTO();
        } catch (PlayerExistsException | InsufficientResourcesException | InvalidTypeException e) {
            throw new AcceptTradeException(e.getMessage());
        }
    }

    /**
     * Performs a maritime trade (trade with the bank)
     *
     * @throws MaritimeTradeException
     */
    @Override
    public void maritimeTrade(int gameID, MaritimeTradeDTO dto) throws MaritimeTradeException {
        try {
            gameManager.getGameByID(gameID).maritimeTrade(dto.getPlayerIndex(), dto.getRatio(), convert(dto.getInputResource()), convert(dto.getOutputResource()));
        }catch(InvalidPlayerException e){}
        catch(InvalidTypeException e){}
        catch(InsufficientResourcesException e){}
        catch(PlayerExistsException e){}
    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @throws DiscardCardsException
     */
    @Override
    public GameModelDTO discardCards(int gameID, DiscardCardsDTO dto) throws DiscardCardsException {
        List<ResourceType> cards = new ArrayList<>();
        for(int i=0; i<dto.getBrickCount(); i++){cards.add(ResourceType.BRICK);}
        for(int i=0; i<dto.getWoodCount(); i++){cards.add(ResourceType.WOOD);}
        for(int i=0; i<dto.getOreCount(); i++){cards.add(ResourceType.ORE);}
        for(int i=0; i<dto.getWheatCount(); i++){cards.add(ResourceType.WHEAT);}
        for(int i=0; i<dto.getSheepCount(); i++){cards.add(ResourceType.SHEEP);}
        try {
            gameManager.getGameByID(gameID).discardCards(dto.getPlayerIndex(), cards);
            return gameManager.getGameByID(gameID).getDTO();
        }catch(PlayerExistsException | InvalidTypeException | InsufficientResourcesException e){
            throw new DiscardCardsException(e.getMessage());
        }
    }

    private ResourceType convert(String type){
        switch(type){
            case "brick":
                return ResourceType.BRICK;
            case "wood":
                return ResourceType.WOOD;
            case "wheat":
                return ResourceType.WHEAT;
            case "sheep":
                return ResourceType.SHEEP;
            case "ore":
                return ResourceType.ORE;
            default:
                return null;
        }
    }
}
