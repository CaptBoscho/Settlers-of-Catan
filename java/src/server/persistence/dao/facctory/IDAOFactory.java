package server.persistence.dao.facctory;

import server.persistence.dao.daos.ICommandDAO;
import server.persistence.dao.daos.IGameDAO;
import server.persistence.dao.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IDAOFactory {
    /**
     * Creates a new UserDAO
     *
     * @return UserDAO that implements IUserDAO
     */
    IUserDAO createUserDAO();

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO that implements IGameDAO
     */
    IGameDAO createGameDAO();

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO that implements ICommandDAO
     */
    ICommandDAO createCommandDAO();
}
