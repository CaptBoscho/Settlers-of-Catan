package server.persistence.dao.facctory;

import server.persistence.dao.daos.IDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IDAOFactory {
    /**
     * Creates a new UserDAO
     *
     * @return UserDAO that implements IDAO
     */
    IDAO createUserDAO();

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO that implements IDAO
     */
    IDAO createGameDAO();

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO that implements IDAO
     */
    IDAO createCommandDAO();
}
