package server.managers;

import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains a list of users. A user is defined as being a unique username/password
 * combination. A user is distinctly different from a Player, which is a user's participation in
 * a single game, not their login account.
 * @author Derek Argueta
 */
public class UserManager {
    private Map<String, String> users;
    private static UserManager instance;

    private UserManager() {
        this.users = new HashMap<>();
    }

    public static UserManager getInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean addUser(final String username, final String password) {

        // check if user already exists
        if(users.containsKey(username)) return false;

        users.put(username, password);
        return true;
    }
}
