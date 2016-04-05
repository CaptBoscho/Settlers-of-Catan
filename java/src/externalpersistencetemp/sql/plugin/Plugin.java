package externalpersistencetemp.sql.plugin;

import externalpersistencetemp.sql.daos.ICommandDAO;
import externalpersistencetemp.sql.daos.IGameDAO;
import externalpersistencetemp.sql.daos.IUserDAO;
import externalpersistencetemp.sql.factory.DAOFactory;
import externalpersistencetemp.sql.factory.IDAOFactory;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 *
 * Persistence Plugin based on Redis
 */
public class Plugin implements IPersistencePlugin {
    private IDAOFactory factory = DAOFactory.getInstance();

    /**
     * Constructor
     */
    public Plugin(){
        // TODO: 4/2/2016 Set up the plugin, eg: database, etc.
    }

    /**
     * Returns a connection to the database
     *
     * @return
     */
    @Override
    public Object getConnection() {
        return null;
    }

    /**
     * Clears the database
     */
    @Override
    public void clear() {

    }

    /**
     * Starts a transaction on the database
     */
    @Override
    public void startTransaction() {

    }

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     */
    @Override
    public void endTransaction(boolean commitTransaction) {

    }

    /**
     * Creates a new UserDAO
     *
     * @return UserDAO
     */
    @Override
    public IUserDAO createUserDAO() {
        return factory.createUserDAO();
    }

    /**
     * Creates a new GameDAO
     *
     * @return GameDAO
     */
    @Override
    public IGameDAO createGameDAO() {
        return factory.createGameDAO();
    }

    /**
     * Creates a new CommandDAO
     *
     * @return CommandDAO
     */
    @Override
    public ICommandDAO createCommandDAO() {
        return factory.createCommandDAO();
    }
}
