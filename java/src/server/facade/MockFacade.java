package server.facade;

import com.google.gson.JsonObject;
import server.exceptions.*;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.DiscardCardsDTO;
import shared.dto.GameModelDTO;
import shared.dto.MaritimeTradeDTO;
import shared.dto.OfferTradeDTO;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

/**
 * Created by Kyle 'TMD' Cornelison on 3/10/2016.
 */
public class MockFacade implements IFacade {

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
     * @param gameID
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
     * @param gameID
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
     * @param gameID
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
     * @param gameID
     * @param player index of the player robbing
     * @param newLocation
     * @param victim index of the player being robbed
     * @return GameModelDTO
     * @throws RobPlayerException
     */
    @Override
    public GameModelDTO robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException {
        return null;
    }

    /**
     * Ends the current player's turn making it the next player's turn
     *
     * @param gameID
     * @param player index of the player
     * @throws FinishTurnException
     */
    @Override
    public void finishTurn(int gameID, int player) throws FinishTurnException {

    }

    /**
     * Buys a new dev card
     *
     * @param gameID
     * @param player index of the player
     * @throws BuyDevCardException
     */
    @Override
    public GameModelDTO buyDevCard(int gameID, int player) throws BuyDevCardException {
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
     */
    @Override
    public void yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException {

    }

    /**
     * Handles playing Road Building
     *
     * @param gameID
     * @param player      index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @return GameModelDTO
     * @throws RoadBuildingException
     */
    @Override
    public GameModelDTO roadBuilding(int gameID, int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException {
        return null;
    }

    /**
     * Handles playing Soldier
     *
     * @param gameID
     * @param player      index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @return GameModelDTO
     * @throws SoldierException
     */
    @Override
    public GameModelDTO soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException {
        return null;
    }

    /**
     * Handles playing Monopoly
     *
     * @param gameID
     * @param player   index of the player
     * @param resource resource to take
     * @throws MonopolyException
     */
    @Override
    public GameModelDTO monopoly(int gameID, int player, ResourceType resource) throws MonopolyException {
        return null;
    }

    /**
     * Handles playing Monument
     *
     * @param gameID
     * @param player index of the player
     * @throws MonumentException
     */
    @Override
    public void monument(int gameID, int player) throws MonumentException {

    }

    /**
     * Builds a road
     *
     * @param gameID
     * @param player index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildRoadException
     */
    @Override
    public GameModelDTO buildRoad(int gameID, int player, EdgeLocation location) throws BuildRoadException {
        return null;
    }

    /**
     * Builds a settlement
     *
     * @param gameID
     * @param player index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildSettlementException
     */
    @Override
    public GameModelDTO buildSettlement(int gameID, int player, VertexLocation location) throws BuildSettlementException {
        return null;
    }

    /**
     * Builds a city
     *
     * @param gameID
     * @param player   index of the player
     * @param location
     * @return GameModelDTO
     * @throws BuildCityException
     */
    @Override
    public GameModelDTO buildCity(int gameID, int player, VertexLocation location) throws BuildCityException {
        return null;
    }

    /**
     * Offers a trade to the specified player
     *
     * @param gameID
     * @param dto
     * @throws OfferTradeException
     */
    @Override
    public GameModelDTO offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException {
        return null;
    }

    /**
     * Accepts a trade offer
     *
     * @param gameID
     * @param player     index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     */
    @Override
    public GameModelDTO acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException {
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
    public void maritimeTrade(int gameID, MaritimeTradeDTO dto) throws MaritimeTradeException {

    }

    /**
     * Discards the specified cards from the player's hand
     *
     * @param gameID
     * @param dto
     * @throws DiscardCardsException
     */
    @Override
    public GameModelDTO discardCards(int gameID, DiscardCardsDTO dto) throws DiscardCardsException {
        return null;
    }
}
