package client.services;

import client.data.GameInfo;
import shared.definitions.CatanColor;
import shared.definitions.ClientModel;
import shared.definitions.ResourceType;
import shared.dto.AuthDTO;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.game.trade.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the common interface that both the real and mock servers implement. By providing this, we can substitute the
 * real or mock server as needed for testing.
 *
 * @author Derek Argueta
 */
public interface IServer {


    //////////////// authentication services /////////////////////

    /**
     * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    public boolean authenticateUser(AuthDTO auth);

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    public boolean registerUser(AuthDTO auth);

    //////////////////// game services /////////////////////////////

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    public List<GameInfo> getAllGames();

    /**
     * Creates a new game with a POST request
     *
     * @param randomTiles Whether the tiles should be randomly placed
     * @param randomNumbers Whether the numbers should be randomly placed
     * @param randomPorts Whether the port should be randomly placed
     * @param name The name of the game
     * @return A new game object
     */
    public GameInfo createNewGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name);

    /**
     *Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param gameId The ID of the game to join
     * @param color ['red' or 'green' or 'blue' or 'yellow' or 'puce' or 'brown' or 'white' or 'purple' or 'orange']:
     *              What color you want to join (or rejoin) as.
     */
    public void joinGame(int gameId, CatanColor color);

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param gameId The ID of the game to save
     * @param name The file name you want to save it under
     */
    public void saveGame(int gameId, String name);

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param gameName The name of the saved game file that you want to load. (The game's ID is restored as well.)
     */
    public void loadGame(String gameName);

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param version The version number of the model that the caller already has. It goes up by one for each command
     *                that is applied. If you send this parameter, you will get a model back only if the current model
     *                is newer than the specified version number. Otherwise, it returns the string "true" to notify the
     *                caller that it already has the current model state.
     * @return A ClientModel object that contains all the information about the state of the game
     */
    public ClientModel getCurrentModel(int version);

    /**
     * Clears out the command history of the current game with a POST request
     */
    public void resetCurrentGame();

    /**
     * Returns a list of commands that have been executed in the current game with a GET request
     */
    public void getAvailableGameCommands();

    /**
     * Executes the specified command list in the current game with a POST request
     *
     * @param gameCommands The list of commands to be executed
     */
    public void executeGameCommands(List<String> gameCommands);

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param aiType The type of AI player to add (currently, LARGEST_ARMY is the only supported type)
     */
    public void addAI(String aiType);

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is the only supported type) with a GET
     * request
     *
     * @return A list of the supported AI types represented as arbitrary strings
     */
    public List<String> getAITypes();

    /////////////////////// move services /////////////////////

    /**
     * Sends a chat message
     *
     * @param playerId The ID of the player who is sending the message
     * @param content The actual message
     * @return The current state of the game
     */
    public ClientModel sendChat(int playerId, String content);

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param playerIndex Who's sending this command (0-3)
     * @param numberRolled what number was rolled (2-12)
     * @return The current state of the game
     */
    public ClientModel rollNumber(int playerIndex, int numberRolled);

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's doing the robbing
     * @param victimIndex The order index of the player to rob
     * @param location The new location of the robber
     * @return The current state of the game
     */
    public ClientModel robPlayer(int playerIndex, int victimIndex, HexLocation location);

    /**
     * Used to finish your turn
     *
     * @param playerIndex Who's sending this command (0-3)
     * @return The current state of the game
     */
    public ClientModel finishTurn(int playerIndex);

    /**
     * Used to buy a development card
     *
     * @param playerIndex Who's playing this dev card
     * @return The current state of the game
     */
    public ClientModel buyDevCard(int playerIndex);

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param playerIndex Who's playing this dev card
     * @param resource1 The first resource being acquired
     * @param resource2 The second resource being acquired
     * @return The current state of the game
     */
    public ClientModel playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2);

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param playerIndex Who's placing the roads
     * @param spot1 The EdgeLocation of the first road
     * @param spot2 The EdgeLocation of the second road
     * @return The current state of the game
     */
    public ClientModel playRoadBuildingCard(int playerIndex, EdgeLocation spot1, EdgeLocation spot2);

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param playerIndex Who's playing this dev card
     * @param victimIndex The index of the player to rob
     * @param location The new location of the robber
     * @return The current state of the game
     */
    public ClientModel playSoldierCard(int playerIndex, int victimIndex, HexLocation location);

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param playerIndex Who's playing this dev card
     * @param resource
     * @return The current state of the game
     */
    public ClientModel playMonopolyCard(int playerIndex, String resource);

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param playerIndex Who's playing this dev card
     * @return The current state of the game
     */
    public ClientModel playMonumentCard(int playerIndex);

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     * @param playerIndex Who's placing the road
     * @param roadLocation The location of the road
     * @param free Whether this is placed for free (setup)
     * @return The current state of the game
     */
    public ClientModel buildRoad(int playerIndex, EdgeLocation roadLocation, boolean free);

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex Who's placing the settlement
     * @param location The location of the new settlement
     * @param free Whether this is placed for free (setup)
     * @return The current state of the game
     */
    public ClientModel buildSettlement(int playerIndex, VertexLocation location, boolean free);

    /**
     * Builds a city at the specified location
     *
     * @param playerIndex Who's placing the city
     * @param location
     * @return The current state of the game
     */
    public ClientModel buildCity(int playerIndex, VertexLocation location);

    /**
     * Offers a domestic trade to another player
     *
     * @param playerIndex Who's sending the offer
     * @param offer What you get (+) and what you give (-)
     * @param receiver Who you're offering the trade to (0-3)
     * @return The current state of the game
     */
    public ClientModel offerTrade(int playerIndex, Trade offer, int receiver);

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param playerIndex Who's accepting / rejecting this trade
     * @param willAccept Whether the player accepted the trade or not
     * @return The current state of the game
     */
    public ClientModel respondToTradeOffer(int playerIndex, boolean willAccept);

    /**
     * Used to execute a maritime trade
     *
     * @param playerIndex Who's doing the trading
     * @param ratio (<i>optional</i>) The ratio of the trade your doing as an integer (ie. put 3 for a 3:1 trade)
     * @param inputResource (<i>optional</i>) What type of resource you're giving
     * @param outputResource (<i>optional</i>) What type of resource you're getting
     * @return The current state of the game
     */
    public ClientModel maritimeTrade(int playerIndex, int ratio, String inputResource, String outputResource);

    /**
     * Discards the specified resource cards
     *
     * @param playerIndex Who's discarding
     * @param resourceList
     * @return The current state of the game
     */
    public ClientModel discardCards(int playerIndex, List<ResourceType> resourceList);

    public boolean changeLogLevel(String logLevel);
}
