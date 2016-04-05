package externalpersistencetemp.sql.factory;

import externalpersistencetemp.sql.daos.*;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class DAOFactory implements IDAOFactory {
    private static IDAOFactory _instance;

    /**
     * Constructor
     */
    private DAOFactory(){

    }

    /**
     * Gets the instance of the DAOFactory
     * @return
     */
    public static IDAOFactory getInstance(){
        if(_instance == null)
            _instance = new DAOFactory();

        return _instance;
    }

    /**
     * Creates a new UserDAO
     *
     * @return UserDAO that implements IDAO
     */
    @Override
    public IUserDAO createUserDAO() {
        return new UserDAO();
    }

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO that implements IDAO
     */
    @Override
    public IGameDAO createGameDAO() {
        return new GameDAO();
    }

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO that implements IDAO
     */
    @Override
    public ICommandDAO createCommandDAO() {
        return new CommandDAO();
    }
}
