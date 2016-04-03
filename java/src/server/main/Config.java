package server.main;

import server.facade.IFacade;
import server.facade.ServerFacade;
import server.persistence.plugins.PersistenceType;
import server.persistence.provider.IPersistenceProvider;
import server.persistence.provider.PersistenceProvider;

/**
 * @author Derek Argueta
 */
public class Config {

    // global values - set to default
    public static IFacade facade = ServerFacade.getInstance();
    public static String host = "localhost";
    public static int port = 8081;
    public static PersistenceType persistenceType = PersistenceType.SQL;
}
