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

    public static ArrayList<GameInfo> getAllGames() {
        final String endpoint = PRE_GAME_BASE_PATH + "/list";
        return new ArrayList<GameInfo>();
    }

    public static GameInfo createNewGame(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
        final String endpoint = PRE_GAME_BASE_PATH + "/create";
        return new GameInfo();
    }

    public static void joinGame(int gameId, CatanColor color) {
        final String endpoint = PRE_GAME_BASE_PATH + "/join";
    }

    public static void saveGame(int gameId, String name) {
        final String endpoint = PRE_GAME_BASE_PATH + "/save";
    }

    public static void loadGame(String gameName) {
        final String endpoint = PRE_GAME_BASE_PATH + "/load";
    }

    public static ClientModel getCurrentModel(int version) {
        final String endpoint = IN_GAME_BASE_PATH + "/model?version=" + version;
        return new ClientModel();
    }

    public static void resetCurrentGame() {
        final String endpoint = IN_GAME_BASE_PATH + "/reset";
    }

    public static void executeGameCommands(List<String> gameCommands) {
        final String endpoint = IN_GAME_BASE_PATH + "/commands";
    }

    public static void getAvailableGameCommands() {
        final String endpoint = IN_GAME_BASE_PATH + "/commands";
    }

    public static void addAI(String aiType) {
        final String endpoint = IN_GAME_BASE_PATH + "/addAI";
    }

    public static List<String> getAITypes() {
        final String endpoint = IN_GAME_BASE_PATH + "/listAI";
        return new ArrayList<String>();
    }
}
