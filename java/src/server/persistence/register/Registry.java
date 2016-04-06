package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.persistence.plugin.IPersistencePlugin;
import java.net.URI;
import java.net.URISyntaxException;

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
     * Registers a new Persistence Plugin
     *
     * @param location Location of the plugin to register
     * @throws PluginExistsException
     * @throws RegisterPluginException
     */
    @Override
    public IPersistencePlugin registerPlugin(String location) throws PluginExistsException, RegisterPluginException {
        //Build the URI
        try {
            URI uri = new URI(location);
            return null;
            // TODO: 4/5/2016 Load the JAR file in and instantiate the class that implements IPersistencePlugin
        } catch (URISyntaxException e) {
            throw new PluginExistsException("Malformed URI!");
        }
    }
}
