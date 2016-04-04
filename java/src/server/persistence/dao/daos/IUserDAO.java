package server.persistence.dao.daos;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IUserDAO {


    /**
     * Creates a user in the database
     * @param username
     * @param userID
     * @param password
     */
    void createUser(String username, String userID, String password);

    /**
     * Finds a user and returns their userID
     * @param userID
     * @param password
     * @return
     */
    int findUser(String userID, String password);

}
