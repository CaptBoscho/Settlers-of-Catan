package externalpersistencetemp.sql.factory;

import externalpersistencetemp.sql.daos.ICommandDAO;
import externalpersistencetemp.sql.daos.IGameDAO;
import externalpersistencetemp.sql.daos.IUserDAO;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IDAOFactory {
    /**
     * Creates a new UserDAO
     *
     * @return UserDAO
     */
    IUserDAO createUserDAO();

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO
     */
    IGameDAO createGameDAO();

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO
     */
    ICommandDAO createCommandDAO();
}
