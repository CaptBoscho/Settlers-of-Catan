package server.facade;

import com.google.gson.JsonObject;
import server.exceptions.*;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.dto.MaritimeTradeDTO;
import shared.dto.OfferTradeDTO;
import shared.exceptions.PlayerExistsException;
import shared.dto.GameModelDTO;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.bank.InvalidTypeException;
import shared.model.cards.resources.ResourceCard;
import shared.model.game.trade.TradePackage;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 * Created by Kyle 'TMD' Cornelison on 3/10/2016.
 */
public interface IFacade {
    //User Methods
    //================================
    /**
     * Logs a player into the server
     * @param username
     * @param password
     * @throws LoginException
     */
    void login(String username, String password) throws LoginException;

    /**
     * Registers a user
     * @param username
     * @param password
     * @throws RegisterException
     */
    void register(String username, String password) throws RegisterException;


    //Game Methods
    //================================
    /**
     * Adds an AI to the game
     * @param aiType
     * @throws AddAIException
     */
    void addAI(int gameID, Object aiType) throws AddAIException;

    /**
     * List the available AI types
     * @throws ListAIException
     */
    void listAI() throws ListAIException;


    //Games Methods
    //================================
    /**
     * List the current games
     * @return info on the current games
     * @throws ListException
     */
    JsonObject list() throws ListException;

    /**
     * Creates a new game
     * @param name
     * @param randomTiles
     * @param randomNumbers
     * @param randomPorts
     * @return
     * @throws CreateGameException
     */
    JsonObject create(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) throws CreateGameException;

    /**
     * Joins a player to the specified game
     * @param gameID
     * @param color
     * @throws JoinGameException
     */
    void join(int gameID, CatanColor color) throws JoinGameException;


    //Move Methods
    //================================
    /**
     * Sends a chat message
     * @param player index of the player
     * @param message
     * @throws SendChatException
     */
    void sendChat(int gameID, int player, String message) throws SendChatException;

    /**
     * Rolls the specified value
     * @param player index of the player
     * @param value
     * @throws RollNumberException
     */
    void rollNumber(int gameID, int player, int value) throws RollNumberException;

    /**
     * Robs the specified player
     * @param player index of the player robbing
     * @param newLocation
     * @param victim index of the player being robbed
     * @throws RobPlayerException
     */
    void robPlayer(int gameID, int player, HexLocation newLocation, int victim) throws RobPlayerException;

    /**
     * Ends the current player's turn making it the next player's turn
     * @param player index of the player
     * @throws FinishTurnException
     */
    void finishTurn(int gameID, int player) throws FinishTurnException;

    /**
     * Buys a new dev card
     * @param player index of the player
     * @throws BuyDevCardException
     */
    GameModelDTO buyDevCard(int gameID, int player) throws BuyDevCardException;

    /**
     * Handles playing Year of Plenty
     * @param player index of the player
     * @param resourceOne first resource to receive
     * @param resourceTwo second resource to receive
     * @throws YearOfPlentyException
     */
    void yearOfPlenty(int gameID, int player, ResourceType resourceOne, ResourceType resourceTwo) throws YearOfPlentyException;

    /**
     * Handles playing Road Building
     * @param player index of the player
     * @param locationOne location for the first road
     * @param locationTwo location for the second road
     * @throws RoadBuildingException
     */
    void roadBuilding(int gameID, int player, EdgeLocation locationOne, EdgeLocation locationTwo) throws RoadBuildingException;

    /**
     * Handles playing Soldier
     * @param player index of the player
     * @param newLocation
     * @param victim index of the player being robbed
     * @throws SoldierException
     */
    void soldier(int gameID, int player, HexLocation newLocation, int victim) throws SoldierException;

    /**
     * Handles playing Monopoly
     * @param player index of the player
     * @param resource resource to take
     * @throws MonopolyException
     */
    void monopoly(int gameID, int player, ResourceType resource) throws MonopolyException;

    /**
     * Handles playing Monument
     * @param player index of the player
     * @throws MonumentException
     */
    void monument(int gameID, int player) throws MonumentException;

    /**
     * Builds a road
     * @param player index of the player
     * @param isFree whether the piece is free
     * @param location
     * @throws BuildRoadException
     */
    void buildRoad(int gameID, int player, boolean isFree, EdgeLocation location) throws BuildRoadException;

    /**
     * Builds a settlement
     * @param player index of the player
     * @param isFree whether the piece is free
     * @param location
     * @throws BuildSettlementException
     */
    void buildSettlement(int gameID, int player, boolean isFree, VertexLocation location) throws BuildSettlementException;

    /**
     * Builds a city
     * @param player index of the player
     * @param location
     * @throws BuildCityException
     */
    void buildCity(int gameID, int player, VertexLocation location) throws BuildCityException;

    /**
     * Offers a trade to the specified player
     * @throws OfferTradeException
     */
    void offerTrade(int gameID, OfferTradeDTO dto) throws OfferTradeException;

    /**
     * Accepts a trade offer
     * @param player index of the player accepting the trade
     * @param willAccept whether or not the player accepts
     * @throws AcceptTradeException
     */
    void acceptTrade(int gameID, int player, boolean willAccept) throws AcceptTradeException, PlayerExistsException, InvalidTypeException, InsufficientResourcesException;

    /**
     * Performs a maritime trade (trade with the bank)
     * @throws MaritimeTradeException
     */
    void maritimeTrade(int gameID, MaritimeTradeDTO dto) throws MaritimeTradeException;

    /**
     * Discards the specified cards from the player's hand
     * @param player index of the player discarding
     * @param cardsToDiscard list of cards to be discarded
     * @throws DiscardCardsException
     */
    void discardCards(int gameID, int player, List<ResourceCard> cardsToDiscard) throws DiscardCardsException;
}
