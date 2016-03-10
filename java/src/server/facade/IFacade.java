package server.facade;

import com.google.gson.JsonObject;
import server.exceptions.*;
import shared.definitions.CatanColor;

/**
 * Created by Kyle 'TMD' Cornelison on 3/10/2016.
 */
public interface IFacade {
    //User Methods
    /**
     * Logs a player into the server
     * @param username
     * @param password
     * @throws LoginException
     */
    void login(String username, String password) throws LoginException;

    /**
     * Registers a user
     * @implNote usernames must be unique
     * @param username
     * @param password
     * @throws RegisterException
     */
    void register(String username, String password) throws RegisterException;

    //Game Methods
    /**
     * Adds an AI to the game
     * @param aiType
     * @throws AddAIException
     */
    void addAI(Object aiType) throws AddAIException;

    /**
     * List the available AI types
     * @throws ListAIException
     */
    void listAI() throws ListAIException;

    //Games Methods
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
    void sendChat();
    void rollNumber();
    void robPlayer();
    void finishTurn();
    void buyDevCard();
    void yearOfPlenty();
    void roadBuilding();
    void soldier();
    void monopoly();
    void monument();
    void buildRoad();
    void buildSettlement();
    void buildCity();
    void offerTrade();
    void acceptTrade();
    void maritimeTrade();
    void discardCards();
}
