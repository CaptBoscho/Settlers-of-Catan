package server.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Pack200;

/**
 * This class maintains a list of users. A user is defined as being a unique username/password
 * combination. A user is distinctly different from a Player, which is a user's participation in
 * a single game, not their login account.
 * @author Derek Argueta
 */
public class UserManager {
    private List<UserCredentials> users;
    private static UserManager instance;

    private UserManager() {
        this.users = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if(instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public int getIdForUser(final String username) {
        for(final UserCredentials user : this.users) {
            if(user.getUsername().equals(username)) {
                return user.getUserId();
            }
        }

        return -1;
    }

    public boolean authenticateUser(final String username, final String password) {
        for(final UserCredentials user : this.users) {
            if(user.matches(username, password)) {
                return true;
            }
        }

        return false;
    }

    public boolean addUser(final String username, final String password) {

        // check if user already exists
        for(final UserCredentials user : this.users) {
            if(user.getUsername().equals(username)) {
                return false;
            }
        }

        final UserCredentials newUser = new UserCredentials(username, password, users.size());
        return users.add(newUser);
    }

    public static void reset() {
        instance = new UserManager();
    }
}

/**
 * @author Derek Argueta
 *
 * This class acts as a "struct" to hold user info together. The UserID is equivalent
 * to the user's index in the List where the credentials are held.
 */
class UserCredentials {

    private String username;
    private String password;
    private int userId;

    //// -- Constructors

    UserCredentials(final String username, final String password, final int id) {
        assert username != null;
        assert username.length() > 0;
        assert password != null;
        assert password.length() > 0;
        assert id >= 0;

        this.username = username;
        this.password = password;
        this.userId = id;
    }

    //// -- utility functions

    /**
     * A helper function to ease authentication in the UserManager
     *
     * @param username The username to check against
     * @param password The password to check against
     * @return Whether or not the credentials match
     * @see UserManager
     */
    boolean matches(final String username, final String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    //// -- Getters/Setters

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    int getUserId() {
        return this.userId;
    }
}
