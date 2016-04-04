package server.persistence.register;

import server.persistence.plugins.IPersistencePlugin;
import server.persistence.plugins.PersistenceType;

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
        registry.put(PersistenceType.ROCK_DB, "");
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
     */
    @Override
    public IPersistencePlugin registerPlugin(PersistenceType type){
        IPersistencePlugin plugin;
        String path = registry.get(type);

        return null;
    }
}
