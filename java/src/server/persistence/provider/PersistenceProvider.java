package server.persistence.provider;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.main.Config;
import server.persistence.daos.ICommandDAO;
import server.persistence.daos.IGameDAO;
import server.persistence.daos.IUserDAO;
import server.persistence.plugin.IPersistencePlugin;
import server.persistence.register.IRegistry;
import server.persistence.register.Registry;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {
    private static IPersistenceProvider _instance;

    private IRegistry registry = Registry.getInstance();
    private String pluginLoc = Config.persistenceLoc;
    private IPersistencePlugin plugin;

    /**
     * Default Constructor
     */
    private PersistenceProvider(){ // TODO: 4/2/2016 Handle exceptions
        try {
            this.plugin = registry.registerPlugin(pluginLoc);
        } catch (PluginExistsException | RegisterPluginException e) {
            e.printStackTrace();
        }
    }

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
     * Returns a connection to the database
     *
     * @return
     */
    @Override
    public Object getConnection() {
        return plugin.getConnection();
    }

    /**
     * Clears the database
     */
    @Override
    public void clear() {
        plugin.clear();
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
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements the IUserDAO Interface
     */
    @Override
    public IUserDAO getUserDAO() {
        return ((IUserDAO) plugin.createUserDAO());
    }

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements the IGameDAO Interface
     */
    @Override
    public IGameDAO getGameDAO() {
        return ((IGameDAO) plugin.createGameDAO());
    }

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements the ICommandDAO Interface
     */
    @Override
    public ICommandDAO getCommandDAO() {
        return ((ICommandDAO) plugin.createCommandDAO());
    }
}
