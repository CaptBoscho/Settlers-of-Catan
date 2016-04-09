package server.persistence.provider;

import server.main.Config;
import server.persistence.daos.ICommandDAO;
import server.persistence.daos.IGameDAO;
import server.persistence.daos.IUserDAO;
import server.persistence.plugin.IDatabase;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {
    private static IPersistenceProvider _instance;
    private IDatabase database;

    /**
     * Default Constructor
     */
    private PersistenceProvider(){
        this.database = Config.database;
    }

    /**
     * Gets the instance of the persistence provider
     *
     * @return
     */
    public static IPersistenceProvider getInstance(){
        if(_instance == null)
            _instance = new PersistenceProvider();

        return _instance;
    }

    /**
     * Clears the database
     */
    @Override
    public void clear() {
        database.clear();
    }

    /**
     * Starts a transaction on the database
     */
    @Override
    public void startTransaction() {
        database.startTransaction();
    }

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     */
    @Override
    public void endTransaction(boolean commitTransaction) {
        database.endTransaction(commitTransaction);
    }


    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements the IUserDAO Interface
     */
    @Override
    public IUserDAO getUserDAO() {
        return database.createUserDAO();
    }

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements the IGameDAO Interface
     */
    @Override
    public IGameDAO getGameDAO() {
        return database.createGameDAO();
    }

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements the ICommandDAO Interface
     */
    @Override
    public ICommandDAO getCommandDAO() {
        return database.createCommandDAO();
    }
}
