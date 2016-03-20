package server.managers;

import shared.model.player.Player;

import java.util.List;

/**
 * This class maintains a list of users. A user is defined as being a unique username/password
 * combination. A user is distinctly different from a Player, which is a user's participation in
 * a single game, not their login account.
 * @author Derek Argueta
 */
public class UserManager {
    private List<Player> users;
    private static UserManager instance;

    public UserManager getInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
}
