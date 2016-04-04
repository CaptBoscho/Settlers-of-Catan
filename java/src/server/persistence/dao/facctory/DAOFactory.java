package server.persistence.dao.facctory;

import server.main.Config;
import server.persistence.dao.daos.ICommandDAO;
import server.persistence.dao.daos.IGameDAO;
import server.persistence.dao.daos.IUserDAO;
import server.persistence.dao.daos.rockdb.RockDBUserDAO;
import server.persistence.dao.daos.sql.SQLUserDAO;
import server.persistence.plugins.PersistenceType;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class DAOFactory implements IDAOFactory {
    private static IDAOFactory _instance;

    private PersistenceType type = Config.persistenceType;

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
     * @return UserDAO that implements IUserDAO
     */
    @Override
    public IUserDAO createUserDAO() {
        IUserDAO userDAO;

        switch(type){
            case SQL:
                userDAO = SQLUserDAO.getInstance();
                break;
            case ROCK_DB:
                userDAO = RockDBUserDAO.getInstance();
                break;
            default:
                userDAO = SQLUserDAO.getInstance();
                break;
        }

        return userDAO;
    }

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO that implements IGameDAO
     */
    @Override
    public IGameDAO createGameDAO() {
        return null;
    }

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO that implements ICommandDAO
     */
    @Override
    public ICommandDAO createCommandDAO() {
        return null;
    }
}
