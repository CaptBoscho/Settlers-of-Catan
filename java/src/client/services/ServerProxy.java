package client.services;

import client.data.GameInfo;
import shared.definitions.ClientModel;
import shared.definitions.ResourceType;
import shared.dto.*;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

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
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    @Override
    public boolean authenticateUser(AuthDTO auth) {
        assert auth != null;
        String url = Utils.buildUrl(this.host, this.port) + "/user/login";
        String result = Utils.sendPost(url, auth.toJSON());
        System.out.println(result);
        assert result != null;
        return result.equals("Success");
    }

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    @Override
    public boolean registerUser(AuthDTO auth) {
        assert auth != null;
        String url = Utils.buildUrl(this.host, this.port) + "/user/register";
        String result = Utils.sendPost(url, auth.toJSON());
        System.out.println(result);
        assert result != null;
        return result.equals("Success");
    }

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    @Override
    public List<GameInfo> getAllGames() {
        String url = Utils.buildUrl(this.host, this.port) + "/games/list";
        String result = Utils.sendGet(url);
        assert result != null;
        GameInfoListDTO list = new GameInfoListDTO(result);
        System.out.println(result);
        return list.getList();
    }

    /**
     * Creates a new game with a POST request
     *
     * @param dto The transport object that contains the information required for a new game
     * @return A new game object
     */
    @Override
    public GameInfo createNewGame(CreateGameDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/games/create";
        String result = Utils.sendPost(url, dto.toJSON());
        return null;
    }

    /**
     * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param dto The transport object that contains the information required to join a game
     */
    @Override
    public void joinGame(JoinGameDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/games/join";
        String result = Utils.sendPost(url, dto.toJSON());
    }

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param dto The transport object that contains the information required to save a game
     */
    @Override
    public void saveGame(SaveGameDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/games/save";
    }

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param dto The transport object that contains the information required to load a game
     */
    @Override
    public void loadGame(LoadGameDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/games/load";
    }

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param dto The transport object that contains the information required to get the current model
     * @return A ClientModel object that contains all the information about the state of the game
     */
    @Override
    public ClientModel getCurrentModel(GetCurrentModelDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/game/model";
        String result = Utils.sendGet(url);
        assert result != null;
        // TODO - convert JSON string to ClientModel
        return null;
    }

    /**
     * Clears out the command history of the current game with a POST request
     */
    @Override
    public void resetCurrentGame() {
        String url = Utils.buildUrl(this.host, this.port) + "/game/reset";
    }

    /**
     * Returns a list of commands that have been executed in the current game with a GET request
     */
    @Override
    public void getAvailableGameCommands() {
        String url = Utils.buildUrl(this.host, this.port) + "/game/commands";
        String result = Utils.sendGet(url);
        assert result != null;
    }

    /**
     * Executes the specified command list in the current game with a POST request
     *
     * @param gameCommands The list of commands to be executed
     */
    @Override
    public void executeGameCommands(List<String> gameCommands) {
        String url = Utils.buildUrl(this.host, this.port) + "/game/commands";
    }

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param aiType The type of AI player to add (currently, LARGEST_ARMY is the only supported type)
     */
    @Override
    public void addAI(String aiType) {
        assert aiType != null;
        String url = Utils.buildUrl(this.host, this.port) + "/game/addAI";
    }

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is the only supported type) with a GET
     * request
     *
     * @return A list of the supported AI types represented as arbitrary strings
     */
    @Override
    public List<String> getAITypes() {
        String url = Utils.buildUrl(this.host, this.port) + "/game/listAI";
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
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/sendChat";
        return null;
    }

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param dto The transport object that contains the information required to roll a number
     * @return The current state of the game
     */
    @Override
    public ClientModel rollNumber(RollNumberDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/rollNumber";
        return null;
    }

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to rob a player
     * @return The current state of the game
     */
    @Override
    public ClientModel robPlayer(RobPlayerDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/robPlayer";
        return null;
    }

    /**
     * Used to finish your turn
     *
     * @param dto The transport object that contains the information required for a player to finish their turn
     * @return The current state of the game
     */
    @Override
    public ClientModel finishTurn(FinishTurnDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/finishTurn";
        return null;
    }

    /**
     * Used to buy a development card
     *
     * @param playerIndex Who's playing this dev card
     * @return The current state of the game
     */
    @Override
    public ClientModel buyDevCard(int playerIndex) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/buyDevCard";
        return null;
    }

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param playerIndex Who's playing this dev card
     * @param resource1   The first resource being acquired
     * @param resource2   The second resource being acquired
     * @return The current state of the game
     */
    @Override
    public ClientModel playYearOfPlentyCard(int playerIndex, ResourceType resource1, ResourceType resource2) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/Year_of_Plenty";
        return null;
    }

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param dto The transport object that contains the information required to play the Year of Plenty card
     * @return The current state of the game
     */
    @Override
    public ClientModel playRoadBuildingCard(PlayYOPCardDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/Road_Building";
        return null;
    }

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to play the soldier card
     * @return The current state of the game
     */
    @Override
    public ClientModel playSoldierCard(PlaySoldierCardDTO dto) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/Soldier";
        return null;
    }

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param playerIndex Who's playing this dev card
     * @param resource
     * @return The current state of the game
     */
    @Override
    public ClientModel playMonopolyCard(int playerIndex, String resource) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/Monopoly";
        return null;
    }

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param playerIndex Who's playing this dev card
     * @return The current state of the game
     */
    @Override
    public ClientModel playMonumentCard(int playerIndex) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/Monument";
        return null;
    }

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex  Who's placing the road
     * @param roadLocation The location of the road
     * @param free         Whether this is placed for free (setup)
     * @return The current state of the game
     */
    @Override
    public ClientModel buildRoad(int playerIndex, EdgeLocation roadLocation, boolean free) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/buildRoad";
        return null;
    }

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param playerIndex Who's placing the settlement
     * @param location    The location of the new settlement
     * @param free        Whether this is placed for free (setup)
     * @return The current state of the game
     */
    @Override
    public ClientModel buildSettlement(int playerIndex, VertexLocation location, boolean free) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/buildSettlement";
        return null;
    }

    /**
     * Builds a city at the specified location
     *
     * @param playerIndex Who's placing the city
     * @param location
     * @return The current state of the game
     */
    @Override
    public ClientModel buildCity(int playerIndex, VertexLocation location) {
        String url = Utils.buildUrl(this.host, this.port) + "/moves/buildCity";
        return null;
    }

    /**
     * Offers a domestic trade to another player
     *
     * @param dto The transport object that contains the information required respond to offer a trade
     * @return The current state of the game
     */
    @Override
    public ClientModel offerTrade(OfferTradeDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/offerTrade";
        return null;
    }

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param dto The transport object that contains the information required respond to a trade offer
     * @return The current state of the game
     */
    @Override
    public ClientModel respondToTradeOffer(TradeOfferResponseDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/acceptTrade";
        return null;
    }

    /**
     * Used to execute a maritime trade
     *
     * @param dto The transport object that contains the information required to execute a maritime trade
     * @return The current state of the game
     */
    @Override
    public ClientModel maritimeTrade(MaritimeTradeDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/maritimeTrade";
        return null;
    }

    /**
     * Discards the specified resource cards
     *
     * @param dto The transport object that contains the information required to discard cards
     * @return The current state of the game
     */
    @Override
    public ClientModel discardCards(DiscardCardsDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/moves/discardCards";
        return null;
    }

    /**
     *
     * @param dto The transport object that contains the information required to change the log level of the server
     * @return
     */
    @Override
    public boolean changeLogLevel(ChangeLogLevelDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + "/util/changeLogLevel";
        return false;
    }
}
