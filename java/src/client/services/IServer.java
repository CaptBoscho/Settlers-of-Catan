package client.services;

import client.data.GameInfo;
import shared.definitions.ClientModel;
import shared.dto.*;

import java.util.List;

/**
 * This is the common interface that both the real and mock servers implement.
 * By providing this, we can substitute the real or mock server as needed for
 * testing.
 *
 * @author Derek Argueta
 */
public interface IServer {

    //////////////// authentication services /////////////////////

    void configure(String host, int port);

    /**
     * Validates the player's credentials, and logs them in to the server (i.e.,
     * sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    boolean authenticateUser(AuthDTO auth);

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets
     * their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    boolean registerUser(AuthDTO auth);

    //////////////////// game services /////////////////////////////

    void getLatestPlayers() throws MissingUserCookieException;

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    List<GameInfo> getAllGames();

    /**
     * Creates a new game with a POST request
     *
     * @param dto The transport object that contains the information required
     *            for a new game
     * @return A new game object
     */
    GameInfo createNewGame(CreateGameDTO dto);

    /**
     *Adds (or re-adds) the player to the specified game, and sets their
     * catan.game HTTP cookie
     *
     * @param dto The transport object that contains the information required to
     *            join a game
     */
    String joinGame(JoinGameDTO dto);

    /**
     * Saves the current state of the specified game to a file with a POST
     * request - FOR DEBUGGING
     *
     * @param dto The transport object that contains the information required to
     *            save a game
     */
    boolean saveGame(SaveGameDTO dto);

    /**
     * Loads a previously saved game file to restore the state of a game with a
     * POST request
     *
     * @param dto The transport object that contains the information required to
     *            save a game
     */
    boolean loadGame(LoadGameDTO dto);

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param version The version number of the model that the caller already
     *                has.
     * @return A ClientModel object that contains all the information about the
     * state of the game
     */
    ClientModel getCurrentModel(int version) throws MissingUserCookieException;

    /**
     * Clears out the command history of the current game with a POST request
     */
    void resetCurrentGame();

    /**
     * Returns a list of commands that have been executed in the current game
     * with a GET request
     */
    void getAvailableGameCommands();

    /**
     * Executes the specified command list in the current game with a POST
     * request
     *
     * @param gameCommands The list of commands to be executed
     */
    void executeGameCommands(List<String> gameCommands);

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param dto Transport object with the information needed to add an AI to the game
     * @return
     */
    String addAI(AddAIDTO dto);

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is
     * the only supported type) with a GET
     * request
     *
     * @return A list of the supported AI types represented as arbitrary strings
     */
    List<String> getAITypes(ListAIDTO dto);



    /////////////////////// move services /////////////////////

    /**
     * Sends a chat message
     *
     * @param dto The transport object that contains the information required to
     *            send a message
     */
     void sendChat(SendChatDTO dto) throws MissingUserCookieException;

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param dto The transport object that contains the information required to
     *            roll a number
     */
    void rollNumber(RollNumberDTO dto) throws MissingUserCookieException, CommandExecutionFailed;

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to
     *            rob a player
     */
    void robPlayer(RobPlayerDTO dto) throws MissingUserCookieException;

    /**
     * Used to finish your turn
     *
     * @param dto The transport object that contains the information required
     *            for a player to finish their turn
     */
    void finishTurn(FinishTurnDTO dto) throws MissingUserCookieException;

    /**
     * Used to buy a development card
     *
     * @param dto The transport object that contains the information required to
     *            buy a development card
     */
    void buyDevCard(BuyDevCardDTO dto) throws MissingUserCookieException;

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two
     * specified resources
     *
     * @param dto The transport object that contains the information required to
     *            play the Year of Plenty card
     */
    void playYearOfPlentyCard(PlayYOPCardDTO dto) throws MissingUserCookieException;

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the
     * specified locations
     *
     * @param dto The transport object that contains the information required to
     *            play the Road Building card
     */
    void playRoadBuildingCard(RoadBuildingDTO dto) throws MissingUserCookieException;

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and
     * player to rob
     *
     * @param dto The transport object that contains the information required to
     *            play the soldier card
     */
    void playSoldierCard(PlaySoldierCardDTO dto) throws MissingUserCookieException;

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified
     * resource
     *
     * @param dto The transport object that contains the information required to
     *            play a monopoly card
     */
    void playMonopolyCard(PlayMonopolyDTO dto) throws MissingUserCookieException;

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param dto The transport object that contains the information required to
     *            play a monument card
     */
    void playMonumentCard(PlayMonumentDTO dto) throws MissingUserCookieException;

    /**
     * Builds a road at the specified location. (Set 'free' to true during
     * initial setup.)
     *
     * @param dto The transport object that contains the information required to
     *            build a road
     */
    void buildRoad(BuildRoadDTO dto) throws MissingUserCookieException;

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during
     * initial setup.)
     *
     * @param dto The transport object that contains the information required to
     *            build a settlement
     */
    void buildSettlement(BuildSettlementDTO dto) throws MissingUserCookieException;

    /**
     * Builds a city at the specified location
     *
     * @param dto The transport object that contains the information required to
     *            build a city
     */
    void buildCity(BuildCityDTO dto) throws MissingUserCookieException;

    /**
     * Offers a domestic trade to another player
     *
     * @param dto The transport object that contains the information required
     *            respond to offer a trade
     */
    void offerTrade(OfferTradeDTO dto) throws MissingUserCookieException;

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param dto The transport object that contains the information required
     *            respond to a trade offer
     */
    void respondToTradeOffer(TradeOfferResponseDTO dto) throws MissingUserCookieException;

    /**
     * Used to execute a maritime trade
     *
     * @param dto The transport object that contains the information required to
     *            execute a maritime trade
     */
    void maritimeTrade(MaritimeTradeDTO dto) throws MissingUserCookieException;

    /**
     * Discards the specified resource cards
     *
     * @param dto The transport object that contains the information required to
     *            discard cards
     */
    void discardCards(DiscardCardsDTO dto) throws MissingUserCookieException;

    /**
     *
     * @param dto The transport object that contains the information required to
     *            change the log level of the server
     * @return
     */
    boolean changeLogLevel(ChangeLogLevelDTO dto);
}