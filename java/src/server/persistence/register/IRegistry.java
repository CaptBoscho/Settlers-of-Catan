package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.persistence.plugin.IPersistencePlugin;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IRegistry {
    /**
     * Registers a new Persistence Plugin
     * @param location Location of the plugin to register
     * @throws PluginExistsException
     * @throws RegisterPluginException
     */
    IPersistencePlugin registerPlugin(String location) throws PluginExistsException, RegisterPluginException;
}
