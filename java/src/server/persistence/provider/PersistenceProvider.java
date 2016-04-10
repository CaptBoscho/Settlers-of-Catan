package server.persistence.provider;

import server.persistence.daos.ICommandDAO;
import server.persistence.daos.IGameDAO;
import server.persistence.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {

    private static IPersistenceProvider _instance;

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
        //database.clear();
    }


    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements the IUserDAO Interface
     */
    @Override
    public IUserDAO getUserDAO() {
        //return database.createUserDAO();
        return null;
    }

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements the IGameDAO Interface
     */
    @Override
    public IGameDAO getGameDAO() {
        //return database.createGameDAO();
        return null;
    }

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements the ICommandDAO Interface
     */
    @Override
    public ICommandDAO getCommandDAO() {
        //return database.createCommandDAO();
        return null;
    }
}
