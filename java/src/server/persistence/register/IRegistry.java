package server.persistence.register;

import server.exceptions.PluginExistsException;
import server.exceptions.RegisterPluginException;
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
     * @throws RegisterPluginException
     */
    IDatabase getPlugin(String plugin) throws PluginExistsException;
}
