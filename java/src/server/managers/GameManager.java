package server.managers;

import shared.model.game.Game;
import shared.model.player.PlayerManager;

import java.util.HashMap;
import java.util.List;

/**
 * This class maintains multiple games. Anytime any game-specific info or action is required, the game
 * is looked up so that the server can maintain multiple games.
 * @author Derek Argueta
 */
public class GameManager {

    private HashMap<Integer, Game> games;
    private Game game;

    public GameManager() {
        game = new Game();
    }

    public Game getGameByID(int gameID) {
        return game;
//        return games.get(gameID);
    }

    public void addGame(Game game) {
        games.put(game.getId(), game);
    }

    public void removeGame(int gameID) {
        games.remove(gameID);
    }
}
