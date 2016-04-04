package server.persistence.provider;

import server.exceptions.EndTransactionException;
import server.exceptions.StartTransactionException;
import server.persistence.dao.daos.ICommandDAO;
import server.persistence.dao.daos.IGameDAO;
import server.persistence.dao.daos.IUserDAO;

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
     * @throws StartTransactionException
     */
    void startTransaction() throws StartTransactionException;

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     * @throws EndTransactionException
     */
    void endTransaction(boolean commitTransaction) throws EndTransactionException;
    //endregion

    //region Factory Methods
    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements IUserDAO interface
     */
    IUserDAO getUserDAO();

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements IGameDAO interface
     */
    IGameDAO getGameDAO();

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements ICommandDAO interface
     */
    ICommandDAO getCommandDAO();
    //endregion
}
