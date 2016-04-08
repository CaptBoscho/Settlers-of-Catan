package server.persistence.plugin;

import server.persistence.daos.*;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IDatabase {
    //region Plugin Methods
    /**
     * Returns a connection to the database
     *
     * @return
     */
    Object getConnection(); // TODO: 4/2/2016 Maybe use a wrapper instead of Object...

    /**
     * Clears the database
     */
    void clear();

    /**
     * Starts a transaction on the database
     */
    void startTransaction();

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     */
    void endTransaction(boolean commitTransaction);

    /**
     * Creates a new UserDAO
     *
     * @return UserDAO
     */
    IUserDAO createUserDAO();

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO
     */
    IGameDAO createGameDAO();

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO
     */
    ICommandDAO createCommandDAO();
    //endregion
}
