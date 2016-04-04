package server.persistence.provider;

import server.main.Config;
import server.persistence.plugins.IPersistencePlugin;
import server.persistence.plugins.PersistenceType;
import server.persistence.register.Register;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class PersistenceProvider implements IPersistenceProvider {
    private static IPersistenceProvider _instance;

    //private PersistenceType type = Config.persistenceType;
    private IPersistencePlugin plugin;

    /**
     * Default Constructor
     */
    private PersistenceProvider(){ // TODO: 4/2/2016 Handle exceptions

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

    @Override
    public void startTransaction() {

    }

    @Override
    public void endTransaction(boolean commitTransaction) {

    }

}
