package server.factories;

import server.commands.ICommand;

/**
 * A factory class that creates Games Commands on demand.  Use this class to get a Games Command
 * Created by Danny Harding on 3/10/16.
 */
public class GamesCommandFactory {

    private static GamesCommandFactory instance = null;

    GamesCommandFactory() {

    }

    public static GamesCommandFactory getInstance() {
        if (instance == null) {
            instance = new GamesCommandFactory();
        }

        return instance;
    }

    /**
     * Creates a Games command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        return null;
    }
}
