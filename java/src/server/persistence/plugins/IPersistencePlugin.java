package server.persistence.plugins;

import server.exceptions.EndTransactionException;
import server.exceptions.StartTransactionException;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IPersistencePlugin {
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
}
