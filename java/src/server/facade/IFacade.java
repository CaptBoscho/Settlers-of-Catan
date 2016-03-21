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
public interface IFacade {
    //User Methods
    //================================
    /**
     * Logs a player into the server
     * @param username
     * @param password
     * @throws LoginException
     * @return boolean
     */
    boolean login(String username, String password) throws LoginException;

    /**
     * Registers a user
     * @param username
     * @param password
     * @throws RegisterException
     * @return CommandExecutionResult
     */
    CommandExecutionResult register(String username, String password) throws RegisterException;


    //Game Methods
    //================================
    /**
     * Adds an AI to the game
     * @param type
     * @throws AddAIException
     * @return CommandExecutionResult
     */
    CommandExecutionResult addAI(int gameId, AIType type) throws AddAIException;

    /**
     * List the available AI types
     * @throws ListAIException
     * @return CommandExecutionResult
     */
    CommandExecutionResult listAI(int gameId) throws ListAIException;


    //Games Methods
    //================================
    /**
     * List the current games
     * @return info on the current games
     * @throws ListException
     * @return CommandExecutinResult
     */
    CommandExecutionResult list();

    /**
     * Creates a new game
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     * @return CommandExecutionResult
     * @throws CreateGameException
     */
    CommandExecutionResult create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts);

    /**
     * Joins a player to the specified game
     * @param gameID
     * @param color
     * @throws JoinGameException
     * @return CommandExecutionResult
     */
    CommandExecutionResult join(int gameID, CatanColor color) throws JoinGameException;


    //Move Methods
    //================================
    /**
     * Sends a chat message
     * @param player index of the player
     * @param message
     * @throws SendChatException
     * @return CommandExecutionResult
     */
    CommandExecutionResult sendChat(int gameID, int player, String message) throws SendChatException;

    /**
     * Rolls the specified value
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     * @return CommandExecutionResult
     */
    CommandExecutionResult rollNumber(int gameID, int player, int value) throws RollNumberException;

    /**
     * Robs the specified player
     * @param player index of the player robbing
     * @param newLocation
     * @param victim index of the player being robbed
     * @return CommandExecutionResult
     * @throws RobPlayerException
     */
    CommandExecutionResult robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException;

    /**
     * Ends the current player's turn making it the next player's turn
     * @param player index of the player
     * @throws FinishTurnException
     * @return CommandExecutionResult
     */
    CommandExecutionResult finishTurn(int gameID, int player) throws FinishTurnException;

    /**
     * Buys a new dev card
     * @param player index of the player
     * @throws BuyDevCardException
     * @return CommandExecutionResult
     */
    CommandExecutionResult buyDevCard(int gameID, int player) throws BuyDevCardException;

    /**
     * Handles playing Year of Plenty
     * @param player index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     * @return CommandExecutionResult
     */
    CommandExecutionResult yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException;

    /**
     * Handles playing Road Building
     * @param player index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @return CommandExecutionResult
     * @throws RoadBuildingException
     */
    CommandExecutionResult roadBuilding(int gameID, int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException;

    /**
     * Handles playing Soldier
     * @param player index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @return CommandExecutionResult
     * @throws SoldierException
     */
    CommandExecutionResult soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException;

    /**
     * Handles playing Monopoly
     * @param player index of the player
     * @param resource resource to take
     * @throws MonopolyException
     * @return CommandExecutionResult
     */
    CommandExecutionResult monopoly(int gameID, int player, ResourceType resource) throws MonopolyException;

    /**
     * Handles playing Monument
     * @param player index of the player
     * @throws MonumentException
     * @return CommandExecutionResult
     */
    CommandExecutionResult monument(int gameID, int player) throws MonumentException;

    /**
     * Builds a road
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildRoadException
     */
    CommandExecutionResult buildRoad(int gameID, int player, EdgeLocation location) throws BuildRoadException;

    /**
     * Builds a settlement
     * @param player index of the player
     * @param location
     * @return CommandExecutionResult
     * @throws BuildSettlementException
     */
    CommandExecutionResult buildSettlement(int gameID, int player, VertexLocation location) throws BuildSettlementException;

    /**
     * Builds a city
     * @param gameID id of game
     * @param player index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildCityException
     */
    CommandExecutionResult buildCity(int gameID, int player, VertexLocation location) throws BuildCityException;

    /**
     * Offers a trade to the specified player
     * @throws OfferTradeException
     * @return CommandExecutionResult
     */
    CommandExecutionResult offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException;

    /**
     * Accepts a trade offer
     * @param player index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     * @return CommandExecutionResult
     */
    CommandExecutionResult acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException ;

    /**
     * Performs a maritime trade (trade with the bank)
     * @throws MaritimeTradeException
     * @return CommandExecutionResult
     */
    CommandExecutionResult maritimeTrade(int gameID, MaritimeTradeDTO dto) throws MaritimeTradeException;

    /**
     * Discards the specified cards from the player's hand
     * @param gameID
     * @param dto
     * @throws DiscardCardsException
     * @return CommandExecutionResult
     */
    CommandExecutionResult discardCards(int gameID, DiscardCardsDTO dto) throws DiscardCardsException;
}
