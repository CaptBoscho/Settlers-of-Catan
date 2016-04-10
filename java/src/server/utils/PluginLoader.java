package server.utils;

import server.persistence.IDatabase;
import server.persistence.Plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Derek Argueta
 */
public final class PluginLoader {

    public server.persistence.IDatabase importDatabaseJar(Plugin plugin) {
        assert plugin != null;

        ClassLoader dbInterfaceLoader = IDatabase.class.getClassLoader();
        ClassLoader urlClassLoader = new URLClassLoader(new URL[]{plugin.getDownloadUrl()}, dbInterfaceLoader);
        Class iDb = null;
        try {
            iDb = Class.forName("server.persistence.Database", true, urlClassLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object dbObj = null;
        try {
            assert iDb != null;
            dbObj = iDb.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded " + plugin.getName());
        return (server.persistence.IDatabase)dbObj;
    }
}
