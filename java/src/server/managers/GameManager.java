package server.managers;

import shared.model.game.Game;
import shared.model.player.PlayerManager;

import java.util.List;

/**
 * This class maintains multiple games. Anytime any game-specific info or action is required, the game
 * is looked up so that the server can maintain multiple games.
 * @author Derek Argueta
 */
public class GameManager {

    private List<Game> games;
}
