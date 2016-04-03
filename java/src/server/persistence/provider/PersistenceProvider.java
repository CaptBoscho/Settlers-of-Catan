package server.persistence.provider;

import server.exceptions.EndTransactionException;
import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.exceptions.StartTransactionException;
import server.main.Config;
import server.persistence.dao.daos.IDAO;
import server.persistence.dao.facctory.DAOFactory;
import server.persistence.dao.facctory.IDAOFactory;
import server.persistence.plugins.IPersistencePlugin;
import server.persistence.plugins.PersistenceType;
import server.persistence.register.IRegister;
import server.persistence.register.Register;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {
    private static IPersistenceProvider _instance;

    private IRegister register = Register.getInstance();
    private IDAOFactory factory = DAOFactory.getInstance();
    private PersistenceType type = Config.persistenceType;
    private IPersistencePlugin plugin;

    /**
     * Default Constructor
     */
    private PersistenceProvider(){ // TODO: 4/2/2016 Handle exceptions
        try {
            this.plugin = register.registerPlugin(type);
        } catch (PluginExistsException e) {
            e.printStackTrace();
        } catch (RegisterPluginException e) {
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
     *
     * @throws StartTransactionException
     */
    @Override
    public void startTransaction() throws StartTransactionException {
        plugin.startTransaction();
    }

    /**
     * Ends a transaction on the database
     *
     * @param commitTransaction
     * @throws EndTransactionException
     */
    @Override
    public void endTransaction(boolean commitTransaction) throws EndTransactionException {
        plugin.endTransaction(commitTransaction);
    }

    /**
     * Creates and returns a new UserDAO
     *
     * @return UserDAO which implements IDAO interface
     */
    @Override
    public IDAO getUserDAO() {
        return factory.createUserDAO();
    }

    /**
     * Creates and returns a new GameDAO
     *
     * @return GameDAO which implements IDAO interface
     */
    @Override
    public IDAO getGameDAO() {
        return factory.createGameDAO();
    }

    /**
     * Creates and returns a new CommandDAO
     *
     * @return CommandDAO which implements IDAO interface
     */
    @Override
    public IDAO getCommandDAO() {
        return factory.createCommandDAO();
    }
}
