package server.managers;

import client.data.GameInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import shared.model.game.Game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This class maintains multiple games. Anytime any game-specific info or action is required, the game
 * is looked up so that the server can maintain multiple games.
 * @author Derek Argueta
 */
public class GameManager {

    // -- TODO this code should not be coupled with business logic
    public static String DEFAULT_GAME = "sample/defaultGame.json";
    public static String EMPTY_GAME = "sample/emptyGame.json";

    private static GameManager instance;
    private Map<Integer, Game> games;

    private GameManager() {
        games = new HashMap<>();
    }

    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Game getGameByID(final int gameID) {
        return games.get(gameID);
    }

    public Collection<Game> getAllGames() {
        return games.values();
    }

    public int getNumGames() {
        return this.games.size();
    }

    public List<GameInfo> getGamesInfos() {
        final List<GameInfo> infos = new ArrayList<>();
        for(final Game game : this.games.values()) {
            final GameInfo gameInfo = new GameInfo();
            gameInfo.setId(game.getId());
            gameInfo.setTitle(game.getTitle());
            gameInfo.setPlayers(game.getPlayerInfos());
            infos.add(gameInfo);
        }
        return infos;
    }

    public void addGame(final Game game) {
        games.put(game.getId(), game);
    }

    public static Game makeGameFromFile(String filePath) throws FileNotFoundException {
        try {
            final FileReader reader = new FileReader(filePath);
            final JsonParser jsonParser = new JsonParser();
            final JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);

            return new Game(jsonObject);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    public static void reset() {
        instance = new GameManager();
    }

    public void addGames(final List<Game> games) {
        games.forEach(this::addGame);
    }
}
