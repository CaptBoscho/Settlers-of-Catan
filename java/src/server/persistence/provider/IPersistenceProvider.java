package server.persistence.provider;

import server.exceptions.EndTransactionException;
import server.exceptions.StartTransactionException;
import server.persistence.daos.IDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IPersistenceProvider {
    //region Plugin Methods
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
     * @return UserDAO which implements IDAO interface
     */
    IDAO getUserDAO();

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements IDAO interface
     */
    IDAO getGameDAO();

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements IDAO interface
     */
    IDAO getCommandDAO();
    //endregion
}
