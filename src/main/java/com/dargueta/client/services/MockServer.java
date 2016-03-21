package com.dargueta.client.services;

import client.data.GameInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.definitions.ClientModel;

import java.util.List;

/**
 * @author Derek Argueta
 */
public class MockServer implements IServer {

    // TODO - read directly out of .json files
    private final String noUser = "The catan.user HTTP cookie is missing.  You must login before calling this method.";
    private final String noGame = "The catan.game HTTP cookie is missing.  You must join a game before calling this method.";
    private final String exampleModel = "{\"deck\": {\"yearOfPlenty\": 2,\"monopoly\": 2,\"soldier\": 14,\"roadBuilding\": 2,\"monument\": 5},"
            + "\"map\": {\"hexes\": [{\"location\": {\"x\": 0,\"y\": -2}},{\"resource\": \"brick\",\"location\": {\"x\": 1,\"y\": -2},"
            + "\"number\": 4},{\"resource\": \"wood\",\"location\": {\"x\": 2,\"y\": -2},\"number\": 11},"
            + "{\"resource\": \"brick\",\"location\": {\"x\": -1,\"y\": -1},\"number\": 8},"
            + "{\"resource\": \"wood\",\"location\": {\"x\": 0,\"y\": -1},\"number\": 3},"
            + "{\"resource\": \"ore\",\"location\": {\"x\": 1,\"y\": -1},\"number\": 9},"
            + "{\"resource\": \"sheep\",\"location\": {\"x\": 2,\"y\": -1},\"number\": 12},"
            + "{\"resource\": \"ore\",\"location\": {\"x\": -2,\"y\": 0},\"number\": 5},"
            + "{\"resource\": \"sheep\",\"location\": {\"x\": -1,\"y\": 0},\"number\": 10},"
            + "{\"resource\": \"wheat\",\"location\": {\"x\": 0,\"y\": 0},\"number\": 11},"
            + "{\"resource\": \"brick\",\"location\": {\"x\": 1,\"y\": 0},\"number\": 5},"
            + "{\"resource\": \"wheat\",\"location\": {\"x\": 2,\"y\": 0},\"number\": 6},"
            + "{\"resource\": \"wheat\",\"location\": {\"x\": -2,\"y\": 1},\"number\": 2},"
            + "{\"resource\": \"sheep\",\"location\": {\"x\": -1,\"y\": 1},\"number\": 9},"
            + "{\"resource\": \"wood\",\"location\": {\"x\": 0,\"y\": 1},\"number\": 4},"
            + "{\"resource\": \"sheep\",\"location\": {\"x\": 1,\"y\": 1},\"number\": 10},"
            + "{\"resource\": \"wood\",\"location\": {\"x\": -2,\"y\": 2},\"number\": 6},"
            + "{\"resource\": \"ore\",\"location\": {\"x\": -1,\"y\": 2},\"number\": 3},"
            + "{\"resource\": \"wheat\",\"location\": {\"x\": 0,\"y\": 2},\"number\": 8}],"
            + "\"roads\": [],\"cities\": [],\"settlements\": [],\"radius\": 3,\"ports\": [{\"ratio\": 3,"
            + "\"direction\": \"N\",\"location\": {\"x\": 0,\"y\": 3}},{\"ratio\": 3,\"direction\": \"NW\",\"location\": {\"x\": 2,\"y\": 1}},"
            + "{\"ratio\": 2,\"resource\": \"sheep\",\"direction\": \"NW\",\"location\": {\"x\": 3,\"y\": -1}},"
            + "{\"ratio\": 3,\"direction\": \"SW\",\"location\": {\"x\": 3,\"y\": -3}},"
            + "{\"ratio\": 2,\"resource\": \"wheat\",\"direction\": \"S\",\"location\": {\"x\": -1,\"y\": -2}},"
            + "{\"ratio\": 2,\"resource\": \"wood\",\"direction\": \"NE\",\"location\": {\"x\": -3,\"y\": 2}},"
            + "{\"ratio\": 2,\"resource\": \"brick\",\"direction\": \"NE\",\"location\": {\"x\": -2,\"y\": 3}},"
            + "{\"ratio\": 3,\"direction\": \"SE\",\"location\": {\"x\": -3,\"y\": 0}},"
            + "{\"ratio\": 2,\"resource\": \"ore\",\"direction\": \"S\",\"location\": {\"x\": 1,\"y\": -3}}],"
            + "\"robber\": {\"x\": 0,\"y\": -2}},\"players\": [{\"resources\": {\"brick\": 0,\"wood\": 0,\"sheep\": 0,\"wheat\": 0,\"ore\": 0},"
            + "\"oldDevCards\": {\"yearOfPlenty\": 0,\"monopoly\": 0,\"soldier\": 0,\"roadBuilding\": 0,\"monument\": 0},"
            + "\"newDevCards\": {\"yearOfPlenty\": 0,\"monopoly\": 0,\"soldier\": 0,\"roadBuilding\": 0,\"monument\": 0},"
            + "\"roads\": 15,\"cities\": 4,\"settlements\": 5,\"soldiers\": 0,\"victoryPoints\": 0,\"monuments\": 0,\"playedDevCard\": false,"
            + "\"discarded\": false,\"playerID\": 12,\"playerIndex\": 0,\"name\": \"Test\",\"color\": \"orange\"},null,null,null],"
            + "\"log\": {\"lines\": []},\"chat\": {\"lines\": []},\"bank\": {\"brick\": 24,\"wood\": 24,\"sheep\": 24,\"wheat\": 24,\"ore\": 24},"
            + "\"turnTracker\": {\"status\": \"FirstRound\",\"currentTurn\": 0,\"longestRoad\": -1,\"largestArmy\": -1},"
            + "\"winner\": -1,\"version\": 0}";

    public static IServer getInstance() {
        return new MockServer();
    }

    @Override
    public void configure(String host, int port) {
        
    }

    /**
     * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    @Override
    public boolean authenticateUser(AuthDTO auth) {
        return auth.getUsername().equals("dev");
    }

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    @Override
    public boolean registerUser(AuthDTO auth) {
        // accept any registration for dev/test purposes
        return true;
    }

    @Override
    public void getLatestPlayers() {

    }

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    @Override
    public List<GameInfo> getAllGames() {
        return null;
    }

    /**
     * Creates a new game with a POST request
     *
     * @param dto The transport object that contains the information required for a new game
     * @return A new game object
     */
    @Override
    public GameInfo createNewGame(CreateGameDTO dto) {
        return new GameInfo("{\"name\": \"" + dto.getName() + "\", \"id\": " + ((int)(Math.random()*100)) + " }");
    }

    /**
     * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param dto The transport object that contains the information required to join a game
     */
    @Override
    public String joinGame(JoinGameDTO dto) {
        return "Success";
    }

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param dto The transport object that contains the information required to save a game
     */
    @Override
    public boolean saveGame(SaveGameDTO dto) {
        // TODO - not needed, only for swagger page
        return true;
    }

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param dto The transport object that contains the information required to save a game
     */
    @Override
    public boolean loadGame(LoadGameDTO dto) {
        // TODO - not needed, only for swagger page
        return true;
    }

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param version The version number of the model that the caller already has.
     *
     * @return A ClientModel object that contains all the information about the state of the game
     */
    @Override
    public ClientModel getCurrentModel(int version) {
        return null;
    }

    /**
     * Clears out the command history of the current game with a POST request
     */
    @Override
    public void resetCurrentGame() {
        // TODO - not needed, only for swagger page
    }

    /**
     * Returns a list of commands that have been executed in the current game with a GET request
     */
    @Override
    public void getAvailableGameCommands() {
        // TODO - not needed, only for swagger page
    }

    /**
     * Executes the specified command list in the current game with a POST request
     *
     * @param gameCommands The list of commands to be executed
     */
    @Override
    public void executeGameCommands(List<String> gameCommands) {
        // TODO - not needed, only for swagger page
    }

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param aiType The type of AI player to add (currently, LARGEST_ARMY is the only supported type)
     */
    @Override
    public void addAI(String aiType) {

    }

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is the only supported type) with a GET
     * request
     *
     * @return A list of the supported AI types represented as arbitrary strings
     */
    @Override
    public List<String> getAITypes() {
        return null;
    }

    /**
     * Sends a chat message
     *
     * @param dto The transport object that contains the information required to send a message
     * @return The current state of the game
     */
    @Override
    public ClientModel sendChat(SendChatDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param dto The transport object that contains the information required to roll a number
     * @return The current state of the game
     */
    @Override
    public ClientModel rollNumber(RollNumberDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to rob a player
     * @return The current state of the game
     */
    @Override
    public ClientModel robPlayer(RobPlayerDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Used to finish your turn
     *
     * @param dto The transport object that contains the information required for a player to finish their turn
     * @return The current state of the game
     */
    @Override
    public ClientModel finishTurn(FinishTurnDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Used to buy a development card
     *
     * @param dto The transport object that contains the information required to buy a development card
     * @return The current state of the game
     */
    @Override
    public ClientModel buyDevCard(BuyDevCardDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param dto The transport object that contains the information required to play the Year of Plenty card
     * @return The current state of the game
     */
    @Override
    public ClientModel playYearOfPlentyCard(PlayYOPCardDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param dto The transport object that contains the information required to play the Road Building card
     * @return The current state of the game
     */
    @Override
    public ClientModel playRoadBuildingCard(RoadBuildingDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to play the soldier card
     * @return The current state of the game
     */
    @Override
    public ClientModel playSoldierCard(PlaySoldierCardDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param dto The transport object that contains the information required to play a monopoly card
     * @return The current state of the game
     */
    @Override
    public ClientModel playMonopolyCard(PlayMonopolyDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param dto The transport object that contains the information required to play a monopoly card
     * @return The current state of the game
     */
    @Override
    public ClientModel playMonumentCard(PlayMonumentDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param dto The transport object that contains the information required to build a road
     * @return The current state of the game
     */
    @Override
    public ClientModel buildRoad(BuildRoadDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param dto The transport object that contains the information required to build a settlement
     * @return The current state of the game
     */
    @Override
    public ClientModel buildSettlement(BuildSettlementDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Builds a city at the specified location
     *
     * @param dto The transport object that contains the information required to build a city
     * @return The current state of the game
     */
    @Override
    public ClientModel buildCity(BuildCityDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Offers a domestic trade to another player
     *
     * @param dto The transport object that contains the information required respond to offer a trade
     * @return The current state of the game
     */
    @Override
    public ClientModel offerTrade(OfferTradeDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param dto The transport object that contains the information required respond to a trade offer
     * @return The current state of the game
     */
    @Override
    public ClientModel respondToTradeOffer(TradeOfferResponseDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Used to execute a maritime trade
     *
     * @param dto The transport object that contains the information required to execute a maritime trade
     * @return The current state of the game
     */
    @Override
    public ClientModel maritimeTrade(MaritimeTradeDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     * Discards the specified resource cards
     *
     * @param dto The transport object that contains the information required to discard cards
     * @return The current state of the game
     */
    @Override
    public ClientModel discardCards(DiscardCardsDTO dto) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(this.exampleModel).getAsJsonObject();
        return new ClientModel(obj);
    }

    /**
     *
     * @param dto The transport object that contains the information required to change the log level of the server
     * @return
     */
    @Override
    public boolean changeLogLevel(ChangeLogLevelDTO dto) {
        // TODO - not needed, only for swagger page
        return false;
    }
}