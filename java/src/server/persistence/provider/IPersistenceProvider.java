package server.persistence.provider;

import server.persistence.daos.*;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IPersistenceProvider {
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
     *
     */
    void startTransaction();

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     */
    void endTransaction(boolean commitTransaction);
    //endregion

    //region Factory Methods
    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements IUserDAO
     */
    IUserDAO getUserDAO();

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements IGameDAO
     */
    IGameDAO getGameDAO();

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements ICommandDAO
     */
    ICommandDAO getCommandDAO();
    //endregion
}
