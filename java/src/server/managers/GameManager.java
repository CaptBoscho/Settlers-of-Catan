package server.managers;

import shared.model.game.Game;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * This class maintains multiple games. Anytime any game-specific info or action is required, the game
 * is looked up so that the server can maintain multiple games.
 * @author Derek Argueta
 */
public class GameManager {

    private static GameManager instance;
    private HashMap<Integer, Game> games;

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

    public void addGame(final Game game) {
        games.put(game.getId(), game);
    }
}
