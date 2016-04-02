package server.persistence.provider;

import server.exceptions.EndTransactionException;
import server.exceptions.StartTransactionException;
import server.persistence.daos.IDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {
    private IPersistenceProvider instance;

    /**
     * Default Constructor
     */
    private PersistenceProvider(){

    }

    /**
     * Gets the instance of the persistence provider
     *
     * @return
     */
    public IPersistenceProvider getInstance(){
        if(this.instance == null)
            this.instance = new PersistenceProvider();

        return this.instance;
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

    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements IDAO interface
     */
    @Override
    public IDAO getUserDAO() {
        return null;
    }

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements IDAO interface
     */
    @Override
    public IDAO getGameDAO() {
        return null;
    }

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements IDAO interface
     */
    @Override
    public IDAO getCommandDAO() {
        return null;
    }
}
