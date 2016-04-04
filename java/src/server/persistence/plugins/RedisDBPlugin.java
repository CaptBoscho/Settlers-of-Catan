package server.persistence.plugins;

import server.exceptions.EndTransactionException;
import server.exceptions.StartTransactionException;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 *
 * Persistence Plugin based on RockDB
 */
public class RedisDBPlugin implements IPersistencePlugin {

    /**
     * Constructor
     * @param location Location of the RockDBPlugin
     */
    public RedisDBPlugin(String location){
        // TODO: 4/2/2016 Set up the plugin, eg: database, etc.
    }

    /**
     * Returns a connection to the database
     *
     * @return
     */
    @Override
    public Object getConnection() {
        return null;
    }

    /**
     * Clears the database
     */
    @Override
    public void clear() {

    }

    /**
     * Starts a transaction on the database
     *
     * @throws StartTransactionException
     */
    @Override
    public void startTransaction() throws StartTransactionException {

    }

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     * @throws EndTransactionException
     */
    @Override
    public void endTransaction(boolean commitTransaction) throws EndTransactionException {

    }
}
