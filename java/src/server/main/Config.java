package server.main;

import server.facade.IFacade;
import server.facade.ServerFacade;
import server.persistence.plugin.IDatabase;

/**
 * @author Derek Argueta
 */
public class Config {

    // global values - set to default
    public static IFacade facade = ServerFacade.getInstance();
    public static String host = "localhost";
    public static int port = 8081;
    public static IDatabase database = null;
}
