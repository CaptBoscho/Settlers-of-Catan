package client.services;

import client.data.GameInfo;
import client.facade.Facade;
import client.misc.MessageView;
import client.services.exceptions.BadHttpRequestException;
import com.google.gson.JsonArray;
import client.services.exceptions.UnauthorizedException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.utils.JSONUtils;
import shared.dto.*;
import shared.model.player.PlayerManager;
import java.util.ArrayList;
import java.util.List;

import static shared.definitions.Endpoints.*;

/**
 * This class is used for all network calls. Every request is a POST or GET HTTP request.
 *
 * @author Derek Argueta
 */
public final class ServerProxy implements IServer {
    private String host;
    private int port;
    private static IServer instance = null;
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8081;

    private ServerProxy(final String host, final int port) {
        assert host != null;
        assert host.length() > 0;
        assert port > 0;

        this.host = host;
        this.port = port;
    }

    /**
     * Shows a pop-up with an error message if something goes wrong with any
     * HTTP requests.
     * @param message The message to be displayed to the user.
     */
    private void showMessageViewForHttpError(final String message) {
        MessageView view = new MessageView();
        view.setTitle("HTTP Error");
        view.setMessage(message);
        view.showModal();
    }

    /**
     * Executes the HTTP request and updates the Game model with the result
     * @param url The URL for the request
     * @param dto The DTO with the parameters for the request
     * @throws MissingUserCookieException
     */
    private void executeModelUpdateRequest(final String url, final IDTO dto) throws MissingUserCookieException {
        String result;
        try {
            result = Utils.sendPost(url, dto.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
            return;
        }
        assert result != null;
        if(result.contains("The catan.user HTTP cookie is missing.")) {
            throw new MissingUserCookieException("The catan.user HTTP cookie is missing.");
        }
        JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
        Facade.getInstance().getGame().updateGame(obj);
    }

    @Override
    public void configure(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public static IServer getInstance() {
        if(instance == null) {
            instance = new ServerProxy(DEFAULT_HOST, DEFAULT_PORT);
        }
        return instance;
    }

    /**
     * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param auth The user's credentials, consisting of username/password
     * @return true if the request succeeded
     */
    @Override
    public boolean authenticateUser(final AuthDTO auth) {
        assert auth != null;
        assert auth.getUsername() != null;
        assert auth.toJSON() != null;
        assert auth.toJSON().has("username");
        assert auth.toJSON().has("password");

        final String url = Utils.buildUrl(this.host, this.port) + LOGIN_ENDPOINT;
        String result = null;
        try {
            result = Utils.sendPost(url, auth.toJSON());
        } catch (BadHttpRequestException e) {

            // unauthorization is normal and frequent when logging in, so handle
            // this normally
            if(e instanceof UnauthorizedException) {
                return false;
            }
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
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
    public boolean registerUser(final AuthDTO auth) {
        assert auth != null;
        assert auth.getUsername() != null;
        assert auth.toJSON() != null;
        assert auth.toJSON().has("username");
        assert auth.toJSON().has("password");

        final String url = Utils.buildUrl(this.host, this.port) + REGISTER_ENDPOINT;
        String result = null;
        try {
            result = Utils.sendPost(url, auth.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
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
        final String url = Utils.buildUrl(this.host, this.port) + LIST_GAMES_ENDPOINT;
        final String result = Utils.sendGet(url);
        assert result != null;
        GameInfoListDTO list = new GameInfoListDTO(result);
        return list.getList();
    }

    /**
     * Creates a new game with a POST request
     *
     * @param dto The transport object that contains the information required for a new game
     * @return A new game object
     */
    @Override
    public GameInfo createNewGame(final CreateGameDTO dto) {
        assert dto != null;
        final String url = Utils.buildUrl(this.host, this.port) + CREATE_GAME_ENDPOINT;
        String result = null;
        try {
            result = Utils.sendPost(url, dto.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
        assert result != null;
        return new GameInfo(result);
    }

    /**
     * Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     * TODO - requires HTTP cookie
     *
     * @param dto The transport object that contains the information required to join a game
     */
    @Override
    public String joinGame(final JoinGameDTO dto) {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + JOIN_GAME_ENDPOINT;
        String result = null;
        try {
            result = Utils.sendPost(url, dto.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
        assert result != null;
        return result;
    }

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param dto The transport object that contains the information required to save a game
     */
    @Override
    public boolean saveGame(final SaveGameDTO dto) {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + SAVE_GAME_ENDPOINT;
        final String result;
        try {
            result = Utils.sendPost(url, dto.toJSON());
            assert result != null;
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
            return false;
        }
        return result.equals("Success");
    }

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param dto The transport object that contains the information required to load a game
     */
    @Override
    public boolean loadGame(final LoadGameDTO dto) {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + LOAD_GAME_ENDPOINT;
        final String result;
        try {
            result = Utils.sendPost(url, dto.toJSON());
            assert result != null;
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
            return false;
        }

        return result.equals("Success");
    }

    private JsonObject requestModelJson(final int version) throws MissingUserCookieException {
        final String url = Utils.buildUrl(this.host, this.port) + "/game/model?version=" + version;
        String result = Utils.sendGet(url);
        assert result != null;
        if(result.contains("The catan.user HTTP cookie is missing.")) {
            throw new MissingUserCookieException("The catan.user HTTP cookie is missing");
        } else if(result.contains("catan.game HTTP cookie is missing")) {
            try {
                throw new Exception("catan.game HTTP cookie is missing");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(result.equals("\"true\"") || !JSONUtils.isJSONValid(result)) {
            // already have latest model, don't update anything
            return null;
        }
        return new JsonParser().parse(result).getAsJsonObject();
    }

    /**
     * Returns the current state of the game in JSON format with a GET request
     *
     * @param version The version number of the model that the caller already has.
     * @return A ClientModel object that contains all the information about the state of the game
     */
    @Override
    public void getCurrentModel(final int version) throws MissingUserCookieException {
        final JsonObject obj = this.requestModelJson(version);
        if(obj == null) return;
        Facade.getInstance().getGame().updateGame(obj);
    }

    public void getLatestPlayers() throws MissingUserCookieException {
        JsonObject obj = this.requestModelJson(-1);
        if(obj == null) return;
        PlayerManager tmp = new PlayerManager(obj.getAsJsonArray("players"));
        if(!tmp.equals(Facade.getInstance().getGame().getPlayerManager())) {
            Facade.getInstance().getGame().setPlayerManager(new PlayerManager(obj.getAsJsonArray("players")));
        }
    }

    /**
     * Clears out the command history of the current game with a POST request
     */
    @Override
    public void resetCurrentGame() {
        final String url = Utils.buildUrl(this.host, this.port) + "/game/reset";
        assert url.contains(this.host);
        try {
            String result = Utils.sendPost(url, null);
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
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
    public void executeGameCommands(final List<String> gameCommands) {
        assert gameCommands != null;
        assert gameCommands.size() > 0;
        String url = Utils.buildUrl(this.host, this.port) + "/game/commands";
    }

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param dto Transport object with the information to add an AI to the game.
     */
    @Override
    public String addAI(final AddAIDTO dto) {
        assert dto != null;
        String url = Utils.buildUrl(this.host, this.port) + ADD_AI_ENDPOINT;
        String result = null;

        try {
            result = Utils.sendPost(url, dto.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
        assert result != null;
        return result;
    }

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is the only supported type) with a GET
     * request
     *
     * @return A list of the supported AI types represented as arbitrary strings
     */
    @Override
    public List<String> getAITypes(final ListAIDTO dto) {
        assert dto != null;
        assert dto.toJSON() != null;
        String url = Utils.buildUrl(this.host, this.port) + LIST_AI_ENDPOINT;
        String result;

        try {
            result = Utils.sendPost(url, dto.toJSON());
            assert result != null;
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
            return new ArrayList<>();
        }

        JsonArray arr = new JsonParser().parse(result).getAsJsonArray();
        List<String> availableAIs = new ArrayList<>();
        arr.forEach(ai-> availableAIs.add(ai.getAsJsonObject().get("ai").getAsString()));
        return availableAIs;
    }

    /**
     * Sends a chat message
     *
     * @param dto The transport object that contains the information required to send a message
     */
    @Override
    public void sendChat(final SendChatDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null; // avoid empty request error

        String url = Utils.buildUrl(this.host, this.port) + SEND_CHAT_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Used to roll a number at the beginning of your turn
     *
     * @param dto The transport object that contains the information required to roll a number
     */
    @Override
    public void rollNumber(final RollNumberDTO dto) throws MissingUserCookieException, CommandExecutionFailed {
        assert dto != null;
        assert dto.toJSON() != null;
        String url = Utils.buildUrl(this.host, this.port) + ROLL_NUMBER_ENDPOINT;
        String result;
        try {
            result = Utils.sendPost(url, dto.toJSON());
            assert result != null;
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
            return;
        }
        if(result.contains("The catan.user HTTP cookie is missing.")) {
            throw new MissingUserCookieException("The catan.user HTTP cookie is missing.");
        }
        if(result.contains("Command execution failed")) {
            throw new CommandExecutionFailed();
        }
        JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
        Facade.getInstance().getGame().updateGame(obj);
    }

    /**
     * Moves the robber, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to rob a player
     */
    @Override
    public void robPlayer(final RobPlayerDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + ROB_PLAYER_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Used to finish your turn
     *
     * @param dto The transport object that contains the information required for a player to finish their turn
     */
    @Override
    public void finishTurn(final FinishTurnDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + FINISH_TURN_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Used to buy a development card
     *
     * @param dto The transport object that contains the information required to buy a development card
     */
    @Override
    public void buyDevCard(final BuyDevCardDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + BUY_DEV_CARD_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Plays a 'Year of Plenty' card from the player's hand to gain the two specified resources
     *
     * @param dto The transport object that contains the information required to play the Year of Plenty card
     */
    @Override
    public void playYearOfPlentyCard(PlayYOPCardDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + YEAR_OF_PLENTY_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Plays a 'Road Building' card from your hand to build two roads at the specified locations
     *
     * @param dto The transport object that contains the information required to play the Road Building card
     */
    @Override
    public void playRoadBuildingCard(RoadBuildingDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;
        final String url = Utils.buildUrl(this.host, this.port) + ROAD_BUILDING_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Plays a 'Soldier' from your hand, selecting the new robber position and player to rob
     *
     * @param dto The transport object that contains the information required to play the soldier card
     */
    @Override
    public void playSoldierCard(PlaySoldierCardDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + SOLDIER_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Plays a 'Monopoly' card from your hand to monopolize the specified resource
     *
     * @param dto The transport object that contains the information required to play a monopoly card
     */
    @Override
    public void playMonopolyCard(PlayMonopolyDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + MONOPOLY_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Plays a 'Monument' card from your hand to give you a victory point
     *
     * @param dto The transport object that contains the information required to play a monopoly card
     */
    @Override
    public void playMonumentCard(PlayMonumentDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + MONUMENT_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Builds a road at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param dto The transport object that contains the information required to build a road
     */
    @Override
    public void buildRoad(BuildRoadDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + BUILD_ROAD_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Builds a settlement at the specified location. (Set 'free' to true during initial setup.)
     *
     * @param dto The transport object that contains the information required to build a settlement
     */
    @Override
    public void buildSettlement(BuildSettlementDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        String url = Utils.buildUrl(this.host, this.port) + BUILD_SETTLEMENT_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Builds a city at the specified location
     *
     * @param dto The transport object that contains the information required to build a city
     */
    @Override
    public void buildCity(BuildCityDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + BUILD_CITY_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Offers a domestic trade to another player
     *
     * @param dto The transport object that contains the information required respond to offer a trade
     */
    @Override
    public void offerTrade(final OfferTradeDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + OFFER_TRADE_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Used to accept or reject a trade offered to the player
     *
     * @param dto The transport object that contains the information required respond to a trade offer
     */
    @Override
    public void respondToTradeOffer(final TradeOfferResponseDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + ACCEPT_TRADE_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Used to execute a maritime trade
     *
     * @param dto The transport object that contains the information required to execute a maritime trade
     */
    @Override
    public void maritimeTrade(final MaritimeTradeDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + MARITIME_TRADE_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     * Discards the specified resource cards
     *
     * @param dto The transport object that contains the information required to discard cards
     */
    @Override
    public void discardCards(DiscardCardsDTO dto) throws MissingUserCookieException {
        assert dto != null;
        assert dto.toJSON() != null;

        String url = Utils.buildUrl(this.host, this.port) + DISCARD_CARDS_ENDPOINT;
        this.executeModelUpdateRequest(url, dto);
    }

    /**
     *
     * @param dto The transport object that contains the information required to change the log level of the server
     */
    @Override
    public boolean changeLogLevel(final ChangeLogLevelDTO dto) {
        assert dto != null;
        assert dto.toJSON() != null;

        final String url = Utils.buildUrl(this.host, this.port) + "/util/changeLogLevel";
        try {
            String result = Utils.sendPost(url, dto.toJSON());
        } catch (BadHttpRequestException e) {
            e.printStackTrace();
            this.showMessageViewForHttpError(e.getMessage());
        }
        return false;
    }
}
