package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
import server.persistence.plugins.IPersistencePlugin;
import server.persistence.plugins.PersistenceType;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IRegister {
    /**
     * Registers a new Persistence Plugin
     * @param type Type of Persistence Plugin to register
     * @throws PluginExistsException
     * @throws RegisterPluginException
     */
    IPersistencePlugin registerPlugin(PersistenceType type) throws PluginExistsException, RegisterPluginException;
}
