package server.facade;

import server.commands.CommandExecutionResult;
import server.exceptions.*;
import server.managers.GameManager;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.DiscardCardsDTO;
import shared.dto.MaritimeTradeDTO;
import shared.dto.OfferTradeDTO;
import shared.exceptions.*;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ai.AIType;
import shared.model.bank.InvalidTypeException;
import shared.model.game.Game;

import javax.naming.InsufficientResourcesException;
import java.io.FileNotFoundException;

/**
 * @author Kyle Cornelison
 */
public final class MockFacade implements IFacade {

    public static final int DEFAULT_GAME = 0;
    public static final int EMPTY_GAME = 1;

    private Game defaultGame;
    private Game emptyGame;

    public MockFacade() {
        resetGames();
    }

    /**
     * Logs a player into the server
     *
     * @param username The player's username
     * @param password The player's password
     * @return boolean
     */
    @Override
    public boolean login(String username, String password) {
        return true;
    }

    /**
     * Registers a user
     *
     * @param username The player's username
     * @param password The player's password
     * @return CommandExecutionResult
     * @throws RegisterException
     */
    @Override
    public CommandExecutionResult register(String username, String password) throws RegisterException {
        return new CommandExecutionResult("Success.");
    }

    /**
     * Adds an AI to the game
     *
     * @param gameId
     * @param type
     * @return CommandExecutionResult
     * @throws AddAIException
     */
    @Override
    public CommandExecutionResult addAI(int gameId, AIType type) throws AddAIException {
        return null;
    }

    /**
     * List the available AI types
     *
     * @param gameId
     * @throws ListAIException
     */
    @Override
    public CommandExecutionResult listAI(int gameId) throws ListAIException {
        return null;
    }

    /**
     * List the current games
     *
     * @return CommandExecutionResult
     * @throws ListException
     */
    @Override
    public CommandExecutionResult list() {
        return null;
    }

    /**
     * Creates a new game
     *
     * @param name          The name of the new game
     * @param randomTiles   Whether or not the tiles should be random
     * @param randomNumbers Whether or not the chits should be random
     * @param randomPorts   Whether or not the ports should be random
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
        return null;
    }

    /**
     * Joins a player to the specified game
     *
     * @param gameID   The ID of the game to be joined
     * @param color    The color the player has chosen for this game
     * @param playerId
     * @param username
     * @return CommandExecutionResult
     * @throws JoinGameException
     */
    @Override
    public CommandExecutionResult join(final int gameID, final CatanColor color, final int playerId, final String username) throws JoinGameException {
        return null;
    }

    /**
     * Sends a chat message
     *
     * @param gameID
     * @param player  index of the player
     * @param message
     * @return CommandExecutionResult
     * @throws SendChatException
     */
    @Override
    public CommandExecutionResult sendChat(int gameID, int player, String message) throws SendChatException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Rolls the specified value
     *
     * @param gameID
     * @param player index of the player
     * @param value
     * @return CommandExecutionResult
     * @throws RollNumberException
     */
    @Override
    public CommandExecutionResult rollNumber(int gameID, int player, int value) throws RollNumberException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Robs the specified player
     *
     * @param gameID
     * @param player      index of the player robbing
     * @param newLocation
     * @param victim      index of the player being robbed
     * @return CommandExecutionResult
     * @throws RobPlayerException
     */
    @Override
    public CommandExecutionResult robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException {
        if (gameID == DEFAULT_GAME) {
            try {
                int cardsBefore = defaultGame.getNumberResourceCards(player);
                defaultGame.rob(player,victim,newLocation);
                if(cardsBefore == defaultGame.getNumberResourceCards(player)){
                    resetGames();
                    throw new RobPlayerException("No robbing took place");
                }
                resetGames();
                return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
            } catch (AlreadyRobbedException | InvalidLocationException | InsufficientResourcesException | PlayerExistsException | InvalidTypeException | MoveRobberException e) {
                resetGames();
                throw new RobPlayerException("can't rob");
            }
        } else if (gameID == EMPTY_GAME) {
            try {
                emptyGame.rob(player,victim,newLocation);
                resetGames();
                return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
            } catch (AlreadyRobbedException | InvalidLocationException | InsufficientResourcesException | PlayerExistsException | InvalidTypeException | MoveRobberException e) {
                resetGames();
                throw new RobPlayerException("can't rob");
            }
        } else {
            return null;
        }
    }

    /**
     * Ends the current player's turn making it the next player's turn
     *
     * @param gameID
     * @param player index of the player
     * @return CommandExecutionResult
     * @throws FinishTurnException
     */
    @Override
    public CommandExecutionResult finishTurn(int gameID, int player) throws FinishTurnException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Buys a new dev card
     *
     * @param gameID
     * @param player index of the player
     * @return CommandExecutionResult
     * @throws BuyDevCardException
     */
    @Override
    public CommandExecutionResult buyDevCard(int gameID, int player) throws BuyDevCardException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Handles playing Year of Plenty
     *
     * @param gameID
     * @param player      index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @return CommandExecutionResult
     * @throws YearOfPlentyException
     */
    @Override
    public CommandExecutionResult yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Handles playing Road Building
     *
     * @param gameID
     * @param player      index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @return CommandExecutionResult
     * @throws RoadBuildingException
     */
    @Override
    public CommandExecutionResult roadBuilding(int gameID, int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Handles playing Soldier
     *
     * @param gameID
     * @param player      index of the player
     * @param newLocation
     * @param victim      index of the player being robbed
     * @return CommandExecutionResult
     * @throws SoldierException
     */
    @Override
    public CommandExecutionResult soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException {
        if (gameID == DEFAULT_GAME) {
            try {
                defaultGame.useSoldier(player,victim,newLocation);
                if(!defaultGame.getMap().getRobber().getLocation().equals(newLocation)) {
                    resetGames();
                    throw new SoldierException("Didn't use soldier card");
                }
                resetGames();
                return new CommandExecutionResult(this.defaultGame.toJSON().toString());
            } catch (InvalidTypeException | InsufficientResourcesException | PlayerExistsException | MoveRobberException | DevCardException | AlreadyRobbedException | InvalidLocationException e) {
                resetGames();
                throw new SoldierException("Can't use soldier");
            }
        } else if (gameID == EMPTY_GAME) {
            try {
                emptyGame.useSoldier(player,victim,newLocation);
                resetGames();
                return new CommandExecutionResult(this.emptyGame.toJSON().toString());
            } catch (InvalidTypeException | InsufficientResourcesException | PlayerExistsException | MoveRobberException | DevCardException | AlreadyRobbedException | InvalidLocationException e) {
                resetGames();
                throw new SoldierException("Can't use soldier");
            }
        } else {
            resetGames();
            return null;
        }
    }

    /**
     * Handles playing Monopoly
     *
     * @param gameID
     * @param player   index of the player
     * @param resource resource to take
     * @return CommandExecutionResult
     * @throws MonopolyException
     */
    @Override
    public CommandExecutionResult monopoly(int gameID, int player, ResourceType resource) throws MonopolyException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Handles playing Monument
     *
     * @param gameID
     * @param player index of the player
     * @return CommandExecutionResult
     * @throws MonumentException
     */
    @Override
    public CommandExecutionResult monument(int gameID, int player) throws MonumentException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Builds a road
     *
     * @param gameID
     * @param player   index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildRoadException
     */
    @Override
    public CommandExecutionResult buildRoad(int gameID, int player, EdgeLocation location) throws BuildRoadException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Builds a settlement
     *
     * @param gameID
     * @param player   index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildSettlementException
     */
    @Override
    public CommandExecutionResult buildSettlement(int gameID, int player, VertexLocation location) throws BuildSettlementException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().toString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().toString());
        } else {
            return null;
        }
    }

    /**
     * Builds a city
     *
     * @param gameID
     * @param player   index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildCityException
     */
    @Override
    public CommandExecutionResult buildCity(int gameID, int player, VertexLocation location) throws BuildCityException {
        if (gameID == DEFAULT_GAME) {
            try {
                defaultGame.getMap().buildCity(player, location);
            } catch (StructureException | InvalidLocationException e) {
                throw new BuildCityException(e.getMessage());
            }
            return new CommandExecutionResult(this.defaultGame.toJSON().toString());
        } else if (gameID == EMPTY_GAME) {
            try {
                emptyGame.getMap().buildCity(player, location);
            } catch (StructureException | InvalidLocationException e) {
                throw new BuildCityException(e.getMessage());
            }
            return new CommandExecutionResult(this.emptyGame.toJSON().toString());
        } else {
            return null;
        }
    }

    /**
     * Offers a trade to the specified player
     *
     * @param gameID
     * @param dto
     * @return CommandExecutionResult
     * @throws OfferTradeException
     */
    @Override
    public CommandExecutionResult offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().toString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().toString());
        } else {
            return null;
        }
    }

    /**
     * Accepts a trade offer
     *
     * @param gameID
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @return CommandExecutionResult
     * @throws AcceptTradeException
     */
    @Override
    public CommandExecutionResult acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Performs a maritime trade (trade with the bank)
     *
     * @param gameID
     * @param dto
     * @throws MaritimeTradeException
     */
    @Override
    public CommandExecutionResult maritimeTrade(int gameID, MaritimeTradeDTO dto) throws MaritimeTradeException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @param gameID
     * @param dto
     * @return CommandExecutionResult
     * @throws DiscardCardsException
     */
    @Override
    public CommandExecutionResult discardCards(int gameID, DiscardCardsDTO dto) throws DiscardCardsException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    /**
     * Gets the model
     *
     * @param gameID
     * @param version
     * @return
     * @throws GetModelException
     */
    @Override
    public CommandExecutionResult getModel(int gameID, int version) throws GetModelException {
        if (gameID == DEFAULT_GAME) {
            return new CommandExecutionResult(this.defaultGame.toJSON().getAsString());
        } else if (gameID == EMPTY_GAME) {
            return new CommandExecutionResult(this.emptyGame.toJSON().getAsString());
        } else {
            return null;
        }
    }

    @Override
    public void resetGames() {
        try {
            defaultGame = GameManager.makeGameFromFile(GameManager.DEFAULT_GAME);
            defaultGame.setId(0);
            defaultGame.setTitle("Default Game");
            emptyGame = GameManager.makeGameFromFile(GameManager.EMPTY_GAME);
            emptyGame.setId(1);
            defaultGame.setTitle("Empty Game");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Game getGame(GameType game) {
        if (game == GameType.DEFAULT_GAME) {
            return defaultGame;
        } else if (game == GameType.EMPTY_GAME) {
            return emptyGame;
        } else {
            return null;
        }
    }

    public enum GameType {
        DEFAULT_GAME,
        EMPTY_GAME
    }

}