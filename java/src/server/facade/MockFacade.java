package server.facade;

import server.commands.CommandExecutionResult;
import server.exceptions.*;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.DiscardCardsDTO;
import shared.dto.MaritimeTradeDTO;
import shared.dto.OfferTradeDTO;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ai.AIType;

/**
 * @author Kyle Cornelison
 */
public class MockFacade implements IFacade {

    /**
     * Logs a player into the server
     *
     * @param username The player's username
     * @param password The player's password
     * @return CommandExecutionResult
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
     * @throws RegisterException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult register(String username, String password) throws RegisterException {
        return null;
    }

    /**
     * Adds an AI to the game
     *
     * @param gameId
     * @param type
     * @throws AddAIException
     * @return CommandExecutionResult
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
     * @param name The name of the new game
     * @param randomTiles Whether or not the tiles should be random
     * @param randomNumbers Whether or not the chits should be random
     * @param randomPorts Whether or not the ports should be random
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
        return null;
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
        return null;
    }

    /**
     * Sends a chat message
     *
     * @param gameID
     * @param player  index of the player
     * @param message
     * @throws SendChatException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult sendChat(int gameID, int player, String message) throws SendChatException {
        return null;
    }

    /**
     * Rolls the specified value
     *
     * @param gameID
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult rollNumber(int gameID, int player, int value) throws RollNumberException {
        return null;
    }

    /**
     * Robs the specified player
     *
     * @param gameID
     * @param player index of the player robbing
     * @param newLocation
     * @param victim index of the player being robbed
     * @return CommandExecutionResult
     * @throws RobPlayerException
     */
    @Override
    public CommandExecutionResult robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException {
        return null;
    }

    /**
     * Ends the current player's turn making it the next player's turn
     *
     * @param gameID
     * @param player index of the player
     * @throws FinishTurnException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult finishTurn(int gameID, int player) throws FinishTurnException {
        return null;
    }

    /**
     * Buys a new dev card
     *
     * @param gameID
     * @param player index of the player
     * @throws BuyDevCardException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult buyDevCard(int gameID, int player) throws BuyDevCardException {
        return null;
    }

    /**
     * Handles playing Year of Plenty
     *
     * @param gameID
     * @param player      index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException {
        return null;
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
        return null;
    }

    /**
     * Handles playing Soldier
     *
     * @param gameID
     * @param player      index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @return CommandExecutionResult
     * @throws SoldierException
     */
    @Override
    public CommandExecutionResult soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException {
        return null;
    }

    /**
     * Handles playing Monopoly
     *
     * @param gameID
     * @param player   index of the player
     * @param resource resource to take
     * @throws MonopolyException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult monopoly(int gameID, int player, ResourceType resource) throws MonopolyException {
        return null;
    }

    /**
     * Handles playing Monument
     *
     * @param gameID
     * @param player index of the player
     * @throws MonumentException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult monument(int gameID, int player) throws MonumentException {
        return null;
    }

    /**
     * Builds a road
     *
     * @param gameID
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildRoadException
     */
    @Override
    public CommandExecutionResult buildRoad(int gameID, int player, EdgeLocation location) throws BuildRoadException {
        return null;
    }

    /**
     * Builds a settlement
     *
     * @param gameID
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildSettlementException
     */
    @Override
    public CommandExecutionResult buildSettlement(int gameID, int player, VertexLocation location) throws BuildSettlementException {
        return null;
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
        return null;
    }

    /**
     * Offers a trade to the specified player
     *
     * @param gameID
     * @param dto
     * @throws OfferTradeException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException {
        return null;
    }

    /**
     * Accepts a trade offer
     *
     * @param gameID
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException {
        return null;
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
        return null;
    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @param gameID
     * @param dto
     * @throws DiscardCardsException
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult discardCards(int gameID, DiscardCardsDTO dto) throws DiscardCardsException {
        return null;
    }
}
