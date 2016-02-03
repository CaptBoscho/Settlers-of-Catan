package client.services;

import client.data.GameInfo;
import shared.definitions.ClientModel;
import shared.definitions.ResourceType;
import shared.dto.*;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

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
     * @param dto The transport object that contains the information required for a new game
     * @return A new game object
     */
    public GameInfo createNewGame(CreateGameDTO dto);

    /**
     *Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param dto The transport object that contains the information required to join a game
     */
    public void joinGame(JoinGameDTO dto);

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param dto The transport object that contains the information required to save a game
     */
    public boolean saveGame(SaveGameDTO dto);

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param dto The transport object that contains the information required to save a game
     */
    public boolean loadGame(LoadGameDTO dto);

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param dto The transport object that contains the information required to get the current model
     * @return A ClientModel object that contains all the information about the state of the game
     */
    public ClientModel getCurrentModel(GetCurrentModelDTO dto);

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
     * @param dto The transport object that contains the information required to send a message
     * @return The current state of the game
     */
    public ClientModel sendChat(SendChatDTO dto);

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param dto The transport object that contains the information required to roll a number
     * @return The current state of the game
     */
    public ClientModel rollNumber(RollNumberDTO dto);

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to rob a player
     * @return The current state of the game
     */
    public ClientModel robPlayer(RobPlayerDTO dto);

    /**
     * Used to finish your turn
     *
     * @param dto The transport object that contains the information required for a player to finish their turn
     * @return The current state of the game
     */
    public ClientModel finishTurn(FinishTurnDTO dto);

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
     * @param dto The transport object that contains the information required to play the Year of Plenty card
     * @return The current state of the game
     */
    public ClientModel playRoadBuildingCard(PlayYOPCardDTO dto);

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to play the soldier card
     * @return The current state of the game
     */
    public ClientModel playSoldierCard(PlaySoldierCardDTO dto);

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
     * @param dto The transport object that contains the information required respond to offer a trade
     * @return The current state of the game
     */
    public ClientModel offerTrade(OfferTradeDTO dto);

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param dto The transport object that contains the information required respond to a trade offer
     * @return The current state of the game
     */
    public ClientModel respondToTradeOffer(TradeOfferResponseDTO dto);

    /**
     * Used to execute a maritime trade
     *
     * @param dto The transport object that contains the information required to execute a maritime trade
     * @return The current state of the game
     */
    public ClientModel maritimeTrade(MaritimeTradeDTO dto);

    /**
     * Discards the specified resource cards
     *
     * @param dto The transport object that contains the information required to discard cards
     * @return The current state of the game
     */
    public ClientModel discardCards(DiscardCardsDTO dto);

    /**
     *
     * @param dto The transport object that contains the information required to change the log level of the server
     * @return
     */
    public boolean changeLogLevel(ChangeLogLevelDTO dto);
}