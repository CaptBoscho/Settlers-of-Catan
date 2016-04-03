package server.persistence.dao.facctory;

import server.main.Config;
import server.persistence.dao.daos.IDAO;
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
     * @return UserDAO that implements IDAO
     */
    @Override
    public IDAO createUserDAO() {
        IDAO userDAO;

        switch(type){
            case SQL:
                userDAO = new SQLUserDAO();
                break;
            case ROCK_DB:
                userDAO = new RockDBUserDAO();
                break;
            default:
                userDAO = new SQLUserDAO();
                break;
        }

        return userDAO;
    }

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO that implements IDAO
     */
    @Override
    public IDAO createGameDAO() {
        return null;
    }

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO that implements IDAO
     */
    @Override
    public IDAO createCommandDAO() {
        return null;
    }
}
