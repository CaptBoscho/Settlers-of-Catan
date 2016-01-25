package client.services;

import client.data.GameInfo;
import shared.definitions.CatanColor;
import shared.definitions.ClientModel;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.game.trade.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for all network calls. Every request is a POST or GET HTTP request.
 *
 * @author Derek Argueta
 */
public class ServerProxy implements IServer {
    private String host;
    private int port;

    public ServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param username The user's username
     * @param password The user's password
     * @return true if the request succeeded
     */
    @Override
    public boolean authenticateUser(String username, String password) {
        return false;
    }

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param username The user's username
     * @param password The user's password
     * @return true if the request succeeded
     */
    @Override
    public boolean registerUser(String username, String password) {
        return false;
    }

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    @Override
    public ArrayList<GameInfo> getAllGames() {
        return null;
    }

    /**
     * Creates a new game with a POST request
     *
     * @param randomTiles   Whether the tiles should be randomly placed
     * @param randomNumbers Whether the numbers should be randomly placed
     * @param randomPorts   Whether the port should be randomly placed
     * @param name          The name of the game
     * @return A new game object
     */
    @Override
    public GameInfo createNewGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        return null;
    }

    /**
     * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param gameId The ID of the game to join
     * @param color  ['red' or 'green' or 'blue' or 'yellow' or 'puce' or 'brown' or 'white' or 'purple' or 'orange']:
     */
    @Override
    public void joinGame(int gameId, CatanColor color) {

    }

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param gameId The ID of the game to save
     * @param name   The file name you want to save it under
     */
    @Override
    public void saveGame(int gameId, String name) {

    }

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param gameName The name of the saved game file that you want to load. (The game's ID is restored as well.)
     */
    @Override
    public void loadGame(String gameName) {

    }

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param version The version number of the model that the caller already has. It goes up by one for each command
     *                that is applied. If you send this parameter, you will get a model back only if the current model
     *                is newer than the specified version number. Otherwise, it returns the string "true" to notify the
     *                caller that it already has the current model state.
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

    }

    /**
     * Returns a list of commands that have been executed in the current game with a GET request
     */
    @Override
    public void getAvailableGameCommands() {

    }

    /**
     * Executes the specified command list in the current game with a POST request
     *
     * @param gameCommands The list of commands to be executed
     */
    @Override
    public void executeGameCommands(List<String> gameCommands) {

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
     * @return
     */
    @Override
    public List<String> getAITypes() {
        return null;
    }

    /**
     * Sends a chat message
     *
     * @param playerIndex
     * @param content
     * @return
     */
    @Override
    public ClientModel sendChat(int playerIndex, String content) {
        return null;
    }

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param playerIndex  Who's sending this command (0-3)
     * @param numberRolled what number was rolled (2-12)
     * @return
     */
    @Override
    public ClientModel rollNumber(int playerIndex, int numberRolled) {
        return null;
    }

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's doing the robbing
     * @param victimIndex The order index of the player to rob
     * @param location    The new location of the robber
     * @return
     */
    @Override
    public ClientModel robPlayer(int playerIndex, int victimIndex, HexLocation location) {
        return null;
    }

    /**
     * Used to finish your turn
     *
     * @param playerIndex Who's sending this command (0-3)
     * @return
     */
    @Override
    public ClientModel finishTurn(int playerIndex) {
        return null;
    }

    /**
     * Used to buy a development card
     *
     * @param playerIndex Who's playing this dev card
     * @return
     */
    @Override
    public ClientModel buyDevCard(int playerIndex) {
        return null;
    }

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param playerIndex Who's playing this dev card
     * @param resource1
     * @param resource2
     * @return
     */
    @Override
    public ClientModel playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2) {
        return null;
    }

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param playerIndex Who's placing the roads
     * @param spot1       The EdgeLocation of the first road
     * @param spot2       The EdgeLocation of the second road
     * @return
     */
    @Override
    public ClientModel playRoadBuildingCard(int playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
        return null;
    }

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's playing this dev card
     * @param victimIndex The index of the player to rob
     * @param location    The new location of the robber
     * @return
     */
    @Override
    public ClientModel playSoldierCard(int playerIndex, int victimIndex, HexLocation location) {
        return null;
    }

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param playerIndex Who's playing this dev card
     * @param resource
     * @return
     */
    @Override
    public ClientModel playMonopolyCard(int playerIndex, String resource) {
        return null;
    }

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param playerIndex Who's playing this dev card
     * @return
     */
    @Override
    public ClientModel playMonumentCard(int playerIndex) {
        return null;
    }

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex  Who's placing the road
     * @param roadLocation
     * @param free         Whether this is placed for free (setup)
     * @return
     */
    @Override
    public ClientModel buildRoad(int playerIndex, EdgeLocation roadLocation, boolean free) {
        return null;
    }

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex Who's placing the settlement
     * @param location
     * @param free        Whether this is placed for free (setup)
     * @return
     */
    @Override
    public ClientModel buildSettlement(int playerIndex, VertexLocation location, boolean free) {
        return null;
    }

    /**
     * Builds a city at the specified location
     *
     * @param playerIndex Who's placing the city
     * @param location
     * @return
     */
    @Override
    public ClientModel buildCity(int playerIndex, VertexLocation location) {
        return null;
    }

    /**
     * Offers a domestic trade to another player
     *
     * @param playerIndex Who's sending the offer
     * @param offer       What you get (+) and what you give (-)
     * @param receiver    Who you're offering the trade to (0-3)
     * @return
     */
    @Override
    public ClientModel offerTrade(int playerIndex, Trade offer, int receiver) {
        return null;
    }

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param playerIndex Who's accepting / rejecting this trade
     * @param willAccept  Whether the player accepted the trade or not
     * @return
     */
    @Override
    public ClientModel respondToTradeOffer(int playerIndex, boolean willAccept) {
        return null;
    }

    /**
     * Used to execute a maritime trade
     *
     * @param playerIndex    Who's doing the trading
     * @param ratio          (<i>optional</i>) The ratio of the trade your doing as an integer (ie. put 3 for a 3:1 trade)
     * @param inputResource  (<i>optional</i>) What type of resource you're giving
     * @param outputResource (<i>optional</i>) What type of resource you're getting
     * @return
     */
    @Override
    public ClientModel maritimeTrade(int playerIndex, int ratio, String inputResource, String outputResource) {
        return null;
    }

    /**
     * Discards the specified resource cards
     *
     * @param playerIndex  Who's discarding
     * @param resourceList
     * @return
     */
    @Override
    public ClientModel discardCards(int playerIndex, List<ResourceType> resourceList) {
        return null;
    }

    @Override
    public boolean changeLogLevel(String logLevel) {
        return false;
    }
}
