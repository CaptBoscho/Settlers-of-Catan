package server.persistence.provider;

import server.persistence.daos.*;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IPersistenceProvider {
    //region Plugin Methods
    /**
     * Clears the database
     */
    void clear();

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
