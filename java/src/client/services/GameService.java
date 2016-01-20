package client.services;

import client.data.GameInfo;
import shared.definitions.CatanColor;
import shared.definitions.ClientModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derek on 1/17/16.
 */
public class GameService {

    private static final String PRE_GAME_BASE_PATH = "/games";
    private static final String IN_GAME_BASE_PATH = "/game";

    /**
     * Get a list of all games in progress with a GET request
     *
     * @return A list of the ongoing games
     */
    public static ArrayList<GameInfo> getAllGames() {
        final String endpoint = PRE_GAME_BASE_PATH + "/list";
        return new ArrayList<GameInfo>();
    }

    /**
     * Creates a new game with a POST request
     *
     * @param randomTiles Whether the tiles should be randomly placed
     * @param randomNumbers Whether the numbers should be randomly placed
     * @param randomPorts Whether the port should be randomly placed
     * @param name The name of the game
     * @return A new game object
     */
    public static GameInfo createNewGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        final String endpoint = PRE_GAME_BASE_PATH + "/create";
        return new GameInfo();
    }

    /**
     *Adds (or re-adds) the player to the specified game, and sets their catan.game HTTP cookie
     *
     * @param gameId The ID of the game to join
     * @param color ['red' or 'green' or 'blue' or 'yellow' or 'puce' or 'brown' or 'white' or 'purple' or 'orange']:
     *              What color you want to join (or rejoin) as.
     */
    public static void joinGame(int gameId, CatanColor color) {
        final String endpoint = PRE_GAME_BASE_PATH + "/join";
    }

    /**
     * Saves the current state of the specified game to a file with a POST request - FOR DEBUGGING
     *
     * @param gameId The ID of the game to save
     * @param name The file name you want to save it under
     */
    public static void saveGame(int gameId, String name) {
        final String endpoint = PRE_GAME_BASE_PATH + "/save";
    }

    /**
     * Loads a previously saved game file to restore the state of a game with a POST request
     *
     * @param gameName The name of the saved game file that you want to load. (The game's ID is restored as well.)
     */
    public static void loadGame(String gameName) {
        final String endpoint = PRE_GAME_BASE_PATH + "/load";
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
    public static ClientModel getCurrentModel(int version) {
        final String endpoint = IN_GAME_BASE_PATH + "/model?version=" + version;
        return new ClientModel();
    }

    /**
     * Clears out the command history of the current game with a POST request
     */
    public static void resetCurrentGame() {
        final String endpoint = IN_GAME_BASE_PATH + "/reset";
    }

    /**
     * Executes the specified command list in the current game with a POST request
     *
     * @param gameCommands The list of commands to be executed
     */
    public static void executeGameCommands(List<String> gameCommands) {
        final String endpoint = IN_GAME_BASE_PATH + "/commands";
    }

    /**
     * Returns a list of commands that have been executed in the current game with a GET request
     */
    public static void getAvailableGameCommands() {
        final String endpoint = IN_GAME_BASE_PATH + "/commands";
    }

    /**
     * Adds an AI player to the current game with a POST request
     *
     * @param aiType The type of AI player to add (currently, LARGEST_ARMY is the only supported type)
     */
    public static void addAI(String aiType) {
        final String endpoint = IN_GAME_BASE_PATH + "/addAI";
    }

    /**
     * Returns a list of supported AI player types (currently, LARGEST_ARMY is the only supported type) with a GET
     * request
     *
     * @return
     */
    public static List<String> getAITypes() {
        final String endpoint = IN_GAME_BASE_PATH + "/listAI";
        return new ArrayList<String>();
    }
}
