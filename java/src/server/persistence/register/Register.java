package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.persistence.plugins.IPersistencePlugin;
import server.persistence.plugins.PersistenceType;
import server.persistence.plugins.RedisDBPlugin;
import server.persistence.plugins.RedisDBPlugin;
import server.persistence.plugins.SQLPlugin;
import java.util.Map;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public class Register implements IRegister {
    private static IRegister _instance;
    private Map<PersistenceType, String> registry;

    /**
     * Default Constructor
     */
    private Register(){
        registry.put(PersistenceType.SQL, "");
        registry.put(PersistenceType.REDIS, "");
    }

    /**
     * Gets the instance of the Register
     * @return
     */
    public static IRegister getInstance(){
        if(_instance == null)
            _instance = new Register();

        return _instance;
    }

    /**
     * Registers a new Persistence Plugin
     *
     * @param type Type of Persistence Plugin to register
     * @throws PluginExistsException
     * @throws RegisterPluginException
     */
    @Override
    public IPersistencePlugin registerPlugin(PersistenceType type) throws PluginExistsException, RegisterPluginException {
        IPersistencePlugin plugin;
        String path = registry.get(type);

        switch (type){
            case SQL:
                plugin = new SQLPlugin(path);
                break;
            case REDIS:
                plugin = new RedisDBPlugin(path);
                break;
            default:
                plugin = new SQLPlugin(path);
                break;
        }

        return plugin;
    }
}
