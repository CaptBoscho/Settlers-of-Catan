package server.facade;

import client.data.GameInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.commands.moves.*;
import server.exceptions.*;
import server.main.Config;
import server.managers.GameManager;
import server.managers.UserManager;
import server.persistence.IDatabase;
import server.persistence.PersistenceCoordinator;
import server.persistence.dto.CommandDTO;
import server.persistence.dto.GameDTO;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.*;
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

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kyle Cornelison
 */
public final class ServerFacade implements IFacade {
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

    private HexLocation getModelHexLocation(HexLocation hexLoc) {
        return new HexLocation(hexLoc.getX(), hexLoc.getY()+hexLoc.getX());
    }

    private EdgeLocation getModelEdgeLocation(EdgeLocation edgeLoc) {
        return new EdgeLocation(getModelHexLocation(edgeLoc.getHexLoc()), edgeLoc.getDir());
    }

    private VertexLocation getModelVertexLocation(VertexLocation vertexLoc) {
        return new VertexLocation(getModelHexLocation(vertexLoc.getHexLoc()), vertexLoc.getDir());
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
     * @param username The player's username
     * @param password The player's password
     * @return boolean
     */
    @Override
    public boolean login(final String username, final String password) {
        assert username != null;
        assert username.length() > 0;
        assert password != null;
        assert password.length() > 0;

        return UserManager.getInstance().authenticateUser(username, password);
    }

    /**
     * Registers a user
     *
     * @param username The player's username
     * @param password The player's password
     * @throws RegisterException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult register(final String username, final String password) throws RegisterException {
        assert username != null;
        assert username.length() > 0;
        assert password != null;
        assert password.length() > 0;

        //TODO: make this method work fool, i mean if you want to, you don't have to
        return null;
    }

    /**
     * Adds an AI to the game
     *
     * @param gameId The ID of the game to which an AI is being added
     * @param type The type of AI being added to the game
     * @throws AddAIException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult addAI(final int gameId, final AIType type) throws AddAIException {
        assert gameId >= 0;
        assert gameId < this.gameManager.getNumGames();
        assert type != null;

        // get the game
        final Game game = gameManager.getGameByID(gameId);
        game.incrementVersion();

        if(game.canAddAI()) {
            game.addAI(type);
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("catan.game", String.valueOf(game.getId()));
            return result;
        } else {
            return new CommandExecutionResult("Failure");
        }
    }

    /**
     * List the available AI types
     *
     * @throws ListAIException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult listAI(final int gameId) throws ListAIException {
        assert gameId >= 0;
        assert gameId < this.gameManager.getNumGames();

        final List<AIType> availableAIs = AIFactory.listAITypes();
        final ListAIDTO dto = new ListAIDTO(gameId, availableAIs);
        final String jsonString = dto.toJSONArr().toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * List the current games
     *
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult list() {
        assert this.gameManager != null;

        final List<GameInfo> games = this.gameManager.getGamesInfos();
        final ListGamesDTO dto = new ListGamesDTO(games);
        final String jsonString = dto.toJSONArr().toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Creates a new game
     *
     * @param name The name of the new game
     * @param randomTiles Whether or not the tiles should be random
     * @param randomNumbers Whether or not the chits should be random
     * @param randomPorts Whether or not the ports should be random
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult create(final String name, final boolean randomTiles, final boolean randomNumbers, boolean randomPorts) {
        assert name != null;
        assert name.length() > 0;
        assert this.gameManager != null;

        // check if name is already taken
        for(final Game game : this.gameManager.getAllGames()) {
            if(game.getTitle().equals(name)) {
                final CommandExecutionResult result = new CommandExecutionResult("Game name taken.");
                result.triggerError(400);
                return result;
            }
        }

        // create the game in the model
        final Game game = new Game(name, randomPorts, randomNumbers, randomTiles);
        game.setId(this.gameManager.getNextAvailableId());
//        game.setId(this.gameManager.getNumGames());
        this.gameManager.addGame(game);

        // construct an info object to send back
        final GameInfo gameInfo = game.getAsGameInfo();
        gameInfo.setPlayers(new ArrayList<>());

        // construct the response
        final GameInfoDTO dto = new GameInfoDTO(gameInfo);
        final String jsonString = dto.toJSON().toString();
        final CommandExecutionResult result = new CommandExecutionResult(jsonString);
        result.addCookie("catan.game", Integer.toString(game.getId()));

        return result;
    }

    /**
     * Joins a player to the specified game
     *
     * @param gameID The ID of the game to be joined
     * @param color The color the player has chosen for this game
     * @param playerId
     * @param username
     * @throws JoinGameException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult join(final int gameID, final CatanColor color, final int playerId, final String username) throws JoinGameException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert color != null;
        assert playerId >= 0;
        assert username != null;
        assert username.length() > 0;

        // get the game
        final Game game = this.gameManager.getGameByID(gameID);
        game.incrementVersion();

        if(game.canAddPlayer() && !game.isRejoining(playerId)) {
            game.getPlayerManager().addNewPlayer(username, playerId, color);
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("catan.game", String.valueOf(game.getId()));
            return result;
        } else if(game.isRejoining(playerId)){
            game.getPlayerManager().rejoin(playerId, color);
            CommandExecutionResult result = new CommandExecutionResult("Success");
            result.addCookie("catan.game", String.valueOf(game.getId()));
            return result;
        } else {
            return new CommandExecutionResult("Failure");
        }
    }

    /**
     * Sends a chat message
     *
     * @param player  index of the player
     * @param message
     * @throws SendChatException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult sendChat(final int gameID, final int player, final String message) throws SendChatException {
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
        } catch (PlayerExistsException e) {
            e.printStackTrace();
            throw new SendChatException("Failed to send the chat message!");
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Rolls the specified value
     *
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult rollNumber(final int gameID, final int player, final int value) throws RollNumberException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

        final Game game = gameManager.getGameByID(gameID);
        try {
            if (game.canRollNumber(player)) {
                game.rollNumber(value);
                game.incrementVersion();
            }
        } catch (Exception e) {
            throw new RollNumberException("Error while rolling!");
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Robs the specified player
     *
     * @param player      index of the player robbing
     * @param newLocation
     * @param victim      index of the player being robbed
     * @return CommandExecutionResult
     * @throws RobPlayerException
     */
    @Override
    public CommandExecutionResult robPlayer(final int gameID, final int player, HexLocation newLocation, final int victim) throws RobPlayerException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert newLocation != null;
        assert victim >= 0;

        Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            newLocation = getModelHexLocation(newLocation);
            if(game.canPlaceRobber(player, newLocation)) {
                game.rob(player, victim, newLocation);
            }
        } catch (InvalidTypeException | InsufficientResourcesException | MoveRobberException | AlreadyRobbedException | PlayerExistsException | InvalidLocationException e) {
            throw new RobPlayerException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Ends the current player's turn making it the next player's turn
     *
     * @param player index of the player
     * @throws FinishTurnException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult finishTurn(final int gameID, final int player) throws FinishTurnException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        if(game.canFinishTurn(player)) {
            try {
                game.finishTurn(player);

                //ai players do their thing
                if(game.isAITurn()) {
                    game.playAI();
                }
            } catch (Exception e) {
                throw new FinishTurnException("Failed to end the player's turn!");
            }
        } else {
            throw new FinishTurnException("Player can't end their turn yet!");
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Buys a new dev card
     *
     * @param playerIndex index of the player
     * @throws BuyDevCardException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult buyDevCard(final int gameID, final int playerIndex) throws BuyDevCardException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            if (game.canBuyDevelopmentCard(playerIndex)) {
                game.buyDevelopmentCard(playerIndex);
            }
        } catch (Exception e) {
            throw new BuyDevCardException("Something went wrong while trying to buy a dev card");
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Handles playing Year of Plenty
     *
     * @param playerIndex      index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult yearOfPlenty(final int gameID, final int playerIndex, final ResourceType resourceOne, final ResourceType resourceTwo) throws YearOfPlentyException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert resourceOne != null;
        assert resourceTwo != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            game.useYearOfPlenty(playerIndex, resourceOne, resourceTwo);
        } catch (PlayerExistsException | DevCardException | InsufficientResourcesException | InvalidTypeException e) {
            throw new YearOfPlentyException("yearOfPlenty failed in the model on the server.");
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Handles playing Road Building
     *
     * @param player      index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @return CommandExecutionResult
     * @throws RoadBuildingException
     */
    @Override
    public CommandExecutionResult roadBuilding(final int gameID, final int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert locationOne != null;
        assert locationTwo != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            locationOne = getModelEdgeLocation(locationOne);
            locationTwo = getModelEdgeLocation(locationTwo);
            if(game.canUseRoadBuilding(player)) {
                game.useRoadBuilder(player, locationOne, locationTwo);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException | DevCardException e) {
            throw new RoadBuildingException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Handles playing Soldier
     *
     * @param player index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @return CommandExecutionResult
     * @throws SoldierException
     */
    @Override
    public CommandExecutionResult soldier(final int gameID, final int player, HexLocation newLocation, final int victim) throws SoldierException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert newLocation != null;
        assert victim >= 0;
        assert victim < 4;

        Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try{
            newLocation = getModelHexLocation(newLocation);
            if(game.canUseSoldier(player)) {
                game.useSoldier(player, victim, newLocation);
            }
        } catch(MoveRobberException | InvalidTypeException | InsufficientResourcesException | DevCardException | PlayerExistsException | AlreadyRobbedException | InvalidLocationException e) {
            throw new SoldierException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Handles playing Monopoly
     *
     * @param playerIndex   index of the player
     * @param resource resource to take
     * @throws MonopolyException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult monopoly(final int gameID, final int playerIndex, final ResourceType resource) throws MonopolyException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;
        assert resource != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            game.useMonopoly(playerIndex, resource);
        } catch (PlayerExistsException | DevCardException | InvalidTypeException | InsufficientResourcesException e) {
            throw new MonopolyException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Handles playing Monument
     *
     * @param playerIndex index of the player
     * @throws MonumentException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult monument(final int gameID, final int playerIndex) throws MonumentException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            gameManager.getGameByID(gameID).useMonument(playerIndex);
            gameManager.getGameByID(gameID).incrementVersion();
        } catch (PlayerExistsException | DevCardException e) {
            e.printStackTrace();
            throw new MonumentException(e.getMessage());
        }

        Game game = gameManager.getGameByID(gameID);
        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Builds a road
     *
     * @param player   index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildRoadException
     */
    @Override
    public CommandExecutionResult buildRoad(final int gameID, final int player, EdgeLocation location) throws BuildRoadException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            location = getModelEdgeLocation(location);
            if(game.canInitiateRoad(player, location)) {
                game.initiateRoad(player, location);
            } else if(game.canBuildRoad(player, location)) {
                game.buildRoad(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            e.printStackTrace();
            throw new BuildRoadException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Builds a settlement
     *
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildSettlementException
     */
    @Override
    public CommandExecutionResult buildSettlement(final int gameID, final int player, VertexLocation location) throws BuildSettlementException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            location = getModelVertexLocation(location);
            if(game.canInitiateSettlement(player, location)) {
                game.initiateSettlement(player, location);
            } else if(game.canBuildSettlement(player, location)) {
                game.buildSettlement(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            e.printStackTrace();
            throw new BuildSettlementException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Builds a city
     *
     * @param gameID id of the game
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildCityException
     */
    @Override
    public CommandExecutionResult buildCity(final int gameID, final int player, VertexLocation location) throws BuildCityException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;
        assert location != null;

        final Game game = gameManager.getGameByID(gameID);
        game.incrementVersion();
        try {
            location = getModelVertexLocation(location);
            if(game.canBuildCity(player, location)) {
                game.buildCity(player, location);
            }
        } catch (InvalidPlayerException | InvalidLocationException | PlayerExistsException | StructureException e) {
            throw new BuildCityException(e.getMessage());
        }

        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Offers a trade to the specified player
     *
     * @throws OfferTradeException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult offerTrade(final int gameID, final OfferTradeDTO dto) throws OfferTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert dto != null;

        final int sender = dto.getSender();
        final int receiver = dto.getReceiver();
        final Trade offer = dto.getOffer();
        offer.getPackage1().setPlayerIndex(sender);
        offer.getPackage2().setPlayerIndex(receiver);
        try {
            gameManager.getGameByID(gameID).offerTrade(offer.getPackage1(), offer.getPackage2());
            gameManager.getGameByID(gameID).incrementVersion();
        } catch(InvalidTypeException | PlayerExistsException | InsufficientResourcesException e) {
            e.printStackTrace();
            throw new OfferTradeException(e.getMessage());
        }

        Game game = gameManager.getGameByID(gameID);
        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Accepts a trade offer
     *
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult acceptTrade(final int gameID, final int player, final boolean willAccept) throws AcceptTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert player >= 0;
        assert player < 4;

        try {
            gameManager.getGameByID(gameID).acceptTrade(player,willAccept);
            gameManager.getGameByID(gameID).incrementVersion();
        } catch (InvalidTypeException | Exception e) {
            throw new AcceptTradeException(e.getMessage());
        }

        Game game = gameManager.getGameByID(gameID);
        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Performs a maritime trade (trade with the bank)
     *
     * @throws MaritimeTradeException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult maritimeTrade(final int gameID, final MaritimeTradeDTO dto) throws MaritimeTradeException {
        assert gameID >= 0;
        assert gameID < this.gameManager.getNumGames();
        assert dto != null;

        try {
            ResourceType send = ResourceType.translateFromString(dto.getInputResource());
            ResourceType receive = ResourceType.translateFromString(dto.getOutputResource());
            Game game = gameManager.getGameByID(gameID);
            game.maritimeTrade(dto.getPlayerIndex(), dto.getRatio(), send, receive);
            gameManager.getGameByID(gameID).incrementVersion();
        } catch(InvalidPlayerException | InsufficientResourcesException | InvalidTypeException | PlayerExistsException e){
            throw new MaritimeTradeException(e.getMessage());
        }

        Game game = gameManager.getGameByID(gameID);
        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @throws DiscardCardsException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult discardCards(final int gameID, final DiscardCardsDTO dto) throws DiscardCardsException {
        assert this.gameManager != null;
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
            gameManager.getGameByID(gameID).incrementVersion();
        } catch(Exception | InvalidTypeException e) {
            throw new DiscardCardsException(e.getMessage());
        }

        Game game = gameManager.getGameByID(gameID);
        JsonObject json = game.toJSON();
        String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * Gets the model
     * @param gameID
     * @param version
     * @return CommandExecutionResult
     * @throws GetModelException
     */
    @Override
    public CommandExecutionResult getModel(final int gameID, final int version) throws GetModelException {
        final Game game = gameManager.getGameByID(gameID);
        if(game.getVersion() == version) {
            // TODO - change this absurd stupidity of \"true\"
            return new CommandExecutionResult("\"true\"");
        }

        final JsonObject json = game.toJSON();
        final String jsonString = json.toString();
        return new CommandExecutionResult(jsonString);
    }

    /**
     * retrieves games and users from the database and populates the server.  This function should be run when
     * ServerFacade is constructed so that it can start out with up to date information
     */
    public void importData() {
        final IDatabase database = PersistenceCoordinator.getDatabase();

        userManager.addUsers(database.getUsers());

        // fetch all Game DTOs and store the game in the GameManager
        final List<GameDTO> gameDTOs = database.getAllGames();
        final ArrayList<Game> games = new ArrayList<>();
        for (final GameDTO dto : gameDTOs) {
            final JsonParser parser = new JsonParser();
            final String gameState = dto.getState();
            Game tmpGame = new Game(parser.parse(gameState).getAsJsonObject());
            tmpGame.setTitle(dto.getTitle());
            tmpGame.setId(dto.getGameID());
            games.add(tmpGame);
        }

        gameManager.addGames(games);

        // fetch the command DTOs and execute them on the game.
        for (final GameDTO dto : gameDTOs) {
            final List<CommandDTO> commands = database.getCommands(dto.getGameID());
            PersistenceCoordinator.getInstance().setCommitCount(dto.getGameID(), commands.size());
            for (final CommandDTO commandDTO : commands) {
                ICommand command;
                if(Config.plugin.equals("postgres")){
                    final JsonObject obj = new JsonParser().parse(commandDTO.getCommand()).getAsJsonObject();
                    String type = obj.get("type").getAsString();
                    System.out.println(type);
                    switch(type) {
                        case "AcceptTrade": {
                            command = new AcceptTradeCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "BuildCity": {
                            command = new BuildCityCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "BuildRoad": {
                            command = new BuildRoadCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "BuildSettlement": {
                            command = new BuildSettlementCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "BuyDevCard": {
                            command = new BuyDevCardCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "DiscardCards": {
                            command = new DiscardCardsCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "FinishTurn": {
                            command = new FinishTurnCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "MaritimeTrade": {
                            command = new MaritimeTradeCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "Monopoly": {
                            command = new MonopolyCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "Monument": {
                            command = new MonumentCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "OfferTrade": {
                            command = new OfferTradeCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "RoadBuilding": {
                            command = new RoadBuildingCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "RobPlayer": {
                            command = new RobPlayerCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "RollNumber": {
                            command = new RollNumberCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "SendChat": {
                            command = new SendChatCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "Soldier": {
                            command = new SoldierCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        case "YearOfPlenty": {
                            command = new YearOfPlentyCommand();
                            command.getFromJson(obj.toString());
                            break;
                        }
                        default: {
                            command = new FinishTurnCommand();
                        }
                    }

                } else {
                    command = PersistenceCoordinator.deserializeCommand(commandDTO.getCommand());
                }


                //final ICommand command = PersistenceCoordinator.deserializeCommand(commandDTO.getCommand());

                try {
                    command.execute();
                } catch (CommandExecutionFailedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void resetGames() {
        GameManager.reset();
    }

    @Override
    public Game getGameByID(int gameID) {
        return gameManager.getGameByID(gameID);
    }
}
