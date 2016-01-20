package client.services;

/**
 * Created by derek on 1/17/16.
 *
 * This class handles all functionality regarding server communication for the user - particularly authentication.
 */
public class UserService {

    private static final String BASE_PATH = "/user";

    /**
     * Validates the player's credentials, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param username The user's username
     * @param password The user's password
     * @return true if the request succeeded
     */
    public static boolean authenticateUser(String username, String password) {
        final String endpoint = BASE_PATH + "/login";
        return true;
    }

    /**
     * Creates a new player account, and logs them in to the server (i.e., sets their catan.user HTTP cookie)
     *
     * @param username The user's username
     * @param password The user's password
     * @return true if the request succeeded
     */
    public static boolean registerUser(String username, String password) {
        final String endpoint = BASE_PATH + "/register";
        return true;
    }
}
