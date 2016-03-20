package server.facade;

import com.google.gson.JsonObject;
import server.commands.CommandExecutionResult;
import server.exceptions.*;
import server.managers.GameManager;
import server.managers.UserManager;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.*;
import shared.dto.DiscardCardsDTO;
import shared.dto.MaritimeTradeDTO;
import shared.dto.OfferTradeDTO;
import shared.exceptions.DevCardException;
import shared.exceptions.InvalidPlayerException;
import shared.exceptions.PlayerExistsException;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ai.AIFactory;
import shared.model.ai.AIType;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;
import shared.model.game.MessageLine;
import shared.model.game.trade.Trade;

import javax.accessibility.AccessibleStateSet;
import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle Cornelison
 */
public class ServerFacade implements IFacade {
    private static IFacade _instance;
    private GameManager gameManager;
    private UserManager userManager;

    /**
     * Default Constructor - Private
     */
    private ServerFacade(){
        gameManager = GameManager.getInstance();
        userManager = UserManager.getInstance();
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
    public void login(final String username, final String password) throws LoginException {
        assert username != null;
        assert username.length() > 0;
        assert password != null;
        assert password.length() > 0;
    }

    /**
     * Registers a user
     *
     * @param username
     * @param password
     * @throws RegisterException
     */
    @Override
    public void register(final String username, final String password) throws RegisterException {
        assert username != null;
        assert username.length() > 0;
        assert password != null;
        assert password.length() > 0;
    }

    /**
     * Adds an AI to the game
     *
     * @param type
     * @throws AddAIException
     */
    @Override
    public GameModelDTO addAI(final int gameId, final AIType type) throws AddAIException {
        assert gameId > 0;
        assert gameId < this.gameManager.getNumGames();
        assert type != null;

        final Game game = gameManager.getGameByID(gameId);

        if(game.canAddAI()){
            game.addAI(type);
            return game.getDTO();
        }else{
            throw new AddAIException("AI player can't be added!");
        }
    }

    /**
     * List the available AI types
     *
     * @throws ListAIException
     */
    @Override
    public ListAIDTO listAI(final int gameId) throws ListAIException {
        assert gameId >= 0;
        assert gameId < this.gameManager.getNumGames();

        return new ListAIDTO(AIFactory.listAITypes());
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
    public JsonObject create(final String name, final boolean randomTiles, final boolean randomNumbers, boolean randomPorts) throws CreateGameException {
        assert name != null;
        assert name.length() > 0;

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
    public void join(final int gameID, final CatanColor color) throws JoinGameException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert color != null;

    }

    /**
     * Sends a chat message
     *
     * @param player  index of the player
     * @param message
     * @throws SendChatException
     */
    @Override
    public GameModelDTO sendChat(final int gameID, final int player, final String message) throws SendChatException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert message != null;
        assert message.length() >= 0;

        final Game game = gameManager.getGameByID(gameID);
        try {
            final String playerName = game.getPlayerNameByIndex(player);
            final MessageLine line = new MessageLine(playerName, message);
            game.getChat().addMessage(line);
            return game.getDTO();
        } catch (PlayerExistsException e) {
            e.printStackTrace();
            throw new SendChatException("Failed to send the chat message!");
        }
    }

    /**
     * Rolls the specified value
     *
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     */
    @Override
    public GameModelDTO rollNumber(final int gameID, final int player, final int value) throws RollNumberException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

        final Game game = gameManager.getGameByID(gameID);
        try {
            game.rollNumber(value);
            return game.getDTO();
        } catch (Exception e) {
            throw new RollNumberException("Error while rolling!");
        }
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
    public GameModelDTO robPlayer(final int gameID, final int player, final HexLocation newLocation, final int victim) throws RobPlayerException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
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
    public GameModelDTO finishTurn(final int gameID, final int player) throws FinishTurnException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

        final Game game = gameManager.getGameByID(gameID);
        if(game.canFinishTurn(player)){
            try {
                game.finishTurn(player);
                return game.getDTO();
            } catch (Exception e) {
                throw new FinishTurnException("Failed to end the player's turn!");
            }
        }else{
            throw new FinishTurnException("Player can't end their turn yet!");
        }
    }

    /**
     * Buys a new dev card
     *
     * @param playerIndex index of the player
     * @throws BuyDevCardException
     */
    @Override
    public GameModelDTO buyDevCard(final int gameID, final int playerIndex) throws BuyDevCardException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final Game game = gameManager.getGameByID(gameID);
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
     * @param playerIndex      index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     */
    @Override
    public GameModelDTO yearOfPlenty(final int gameID, final int playerIndex, final ResourceType resourceOne, final ResourceType resourceTwo) throws YearOfPlentyException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert resourceOne != null;
        assert resourceTwo != null;

        try {
            final Game game = gameManager.getGameByID(gameID);
            game.useYearOfPlenty(playerIndex, resourceOne, resourceTwo);
            return game.getDTO();
        } catch (PlayerExistsException | DevCardException | InsufficientResourcesException | InvalidTypeException e) {
            throw new YearOfPlentyException("yearOfPlenty failed in the model on the server.");
        }
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
    public GameModelDTO roadBuilding(final int gameID, final int player, final EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert locationOne != null;
        assert locationTwo != null;

        final Game game = gameManager.getGameByID(gameID);
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
    public GameModelDTO soldier(final int gameID, final int player, final HexLocation newLocation, final int victim) throws SoldierException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert newLocation != null;
        assert victim >= 0;
        assert victim < 4;

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
     * Handles playing Monopoly
     *
     * @param playerIndex   index of the player
     * @param resource resource to take
     * @throws MonopolyException
     */
    @Override
    public GameModelDTO monopoly(final int gameID, final int playerIndex, final ResourceType resource) throws MonopolyException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert resource != null;

        try {
            Game game = gameManager.getGameByID(gameID);
            game.useMonopoly(playerIndex, resource);
            return game.getDTO();
        } catch (PlayerExistsException | DevCardException | InvalidTypeException | InsufficientResourcesException e) {
            throw new MonopolyException(e.getMessage());
        }
    }

    /**
     * Handles playing Monument
     *
     * @param playerIndex index of the player
     * @throws MonumentException
     */
    @Override
    public GameModelDTO monument(final int gameID, final int playerIndex) throws MonumentException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            gameManager.getGameByID(gameID).useMonument(playerIndex);
            return gameManager.getGameByID(gameID).getDTO();
        } catch (PlayerExistsException | DevCardException e) {
            e.printStackTrace();
            throw new MonumentException(e.getMessage());
        }
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
    public CommandExecutionResult buildRoad(final int gameID, final int player, final EdgeLocation location) throws BuildRoadException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canInitiateRoad(player, location)) {
                game.initiateRoad(player, location);
            } else if(game.canBuildRoad(player, location)) {
                game.buildRoad(player, location);
            }
            return new CommandExecutionResult(game.getDTO().toJSON().getAsString());
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
    public GameModelDTO buildSettlement(final int gameID, final int player, final VertexLocation location) throws BuildSettlementException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canInitiateSettlement(player, location)) {
                game.initiateSettlement(player, location);
            } else if(game.canBuildSettlement(player, location)) {
                game.buildSettlement(player, location);
            }
            return game.getDTO();
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
    public CommandExecutionResult buildCity(final int gameID, final int player, final VertexLocation location) throws BuildCityException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        try {
            if(game.canBuildCity(player, location)) {
                game.buildCity(player, location);
            }
            return new CommandExecutionResult(game.getDTO().toJSON().getAsString());
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildCityException(e.getMessage());
        }
    }

    /**
     * Offers a trade to the specified player
     *
     * @throws OfferTradeException
     */
    @Override
    public GameModelDTO offerTrade(final int gameID, final OfferTradeDTO dto) throws OfferTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert dto != null;

        final int sender = dto.getSender();
        final int receiver = dto.getReceiver();
        final Trade offer = dto.getOffer();
        final List<ResourceType> send = offer.getPackage1().getResources();
        final List<ResourceType> receive = offer.getPackage2().getResources();
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
    public GameModelDTO acceptTrade(final int gameID, final int player, final boolean willAccept) throws AcceptTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

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
    public void maritimeTrade(final int gameID, final MaritimeTradeDTO dto) throws MaritimeTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert dto != null;

        try {
            gameManager.getGameByID(gameID).maritimeTrade(dto.getPlayerIndex(), dto.getRatio(), convert(dto.getInputResource()), convert(dto.getOutputResource()));
        } catch(InvalidPlayerException | InsufficientResourcesException | InvalidTypeException | PlayerExistsException ignored){}
    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @throws DiscardCardsException
     */
    @Override
    public GameModelDTO discardCards(final int gameID, final DiscardCardsDTO dto) throws DiscardCardsException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert dto != null;

        final List<ResourceType> cards = new ArrayList<>();
        for(int i = 0; i < dto.getBrickCount(); i++) {
            cards.add(ResourceType.BRICK);
        }
        for(int i = 0; i < dto.getWoodCount(); i++) {
            cards.add(ResourceType.WOOD);
        }
        for(int i = 0; i < dto.getOreCount(); i++) {
            cards.add(ResourceType.ORE);
        }
        for(int i = 0; i < dto.getWheatCount(); i++) {
            cards.add(ResourceType.WHEAT);
        }
        for(int i = 0; i < dto.getSheepCount(); i++) {
            cards.add(ResourceType.SHEEP);
        }
        try {
            gameManager.getGameByID(gameID).discardCards(dto.getPlayerIndex(), cards);
            return gameManager.getGameByID(gameID).getDTO();
        }catch(PlayerExistsException | InvalidTypeException | InsufficientResourcesException e){
            throw new DiscardCardsException(e.getMessage());
        }
    }

    private ResourceType convert(final String type) {
        assert type != null;

        switch(type) {
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
