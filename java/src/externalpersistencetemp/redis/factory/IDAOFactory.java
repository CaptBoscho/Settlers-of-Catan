package externalpersistencetemp.redis.factory;

import externalpersistencetemp.redis.daos.ICommandDAO;
import externalpersistencetemp.redis.daos.IGameDAO;
import externalpersistencetemp.redis.daos.IUserDAO;

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
