package server.persistence.registry;

import server.exceptions.PluginExistsException;
import server.persistence.plugin.IDatabase;

/**
 * Created by Kyle 'TMD' Cornelison on 4/2/2016.
 */
public interface IRegistry {

    /**
     * Checks if the specified plugin exists
     *
     * @param plugin
     * @return
     */
    boolean pluginExists(String plugin);

    /**
     * Gets a database plugin
     *
     * @param plugin
     * @return
     * @throws PluginExistsException
     */
    IDatabase getPlugin(String plugin) throws PluginExistsException;
}
