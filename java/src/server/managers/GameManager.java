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
        try {
            Game game = GameManager.makeGameFromFile(GameManager.DEFAULT_GAME);
            game.setId(0);
            game.setTitle("Default Game");
            addGame(game);
            game = GameManager.makeGameFromFile(GameManager.EMPTY_GAME);
            game.setId(1);
            game.setTitle("Empty Game");
            addGame(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<GameInfo> infos = new ArrayList<>();
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
            FileReader reader = new FileReader(filePath);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(reader);

            return new Game(jsonObject);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    public static void reset() {
        instance = new GameManager();
    }

    public static Map<Integer, Game> getGames() {
        return (Map<Integer, Game>)instance.getAllGames();
    }

    public void addGames(ArrayList<Game> games) {
        games.forEach(this::addGame);
    }
}
