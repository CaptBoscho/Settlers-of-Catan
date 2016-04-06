package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.persistence.plugin.IDatabase;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class Registry implements IRegistry {
    private static IRegistry _instance;

    /**
     * Default Constructor
     */
    private Registry(){

    }

    /**
     * Gets the instance of the Register
     * @return
     */
    public static IRegistry getInstance(){
        if(_instance == null)
            _instance = new Registry();

        return _instance;
    }

    /**
     * Checks if the specified plugin exists
     *
     * @param plugin
     * @return
     */
    @Override
    public boolean pluginExists(String plugin) {
        return false;
    }

    /**
     * Gets a database plugin
     *
     * @param plugin
     * @return
     * @throws RegisterPluginException
     */
    @Override
    public IDatabase getPlugin(String plugin) throws PluginExistsException {
        return null;
    }
}
