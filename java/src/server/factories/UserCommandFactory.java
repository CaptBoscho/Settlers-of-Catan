package server.factories;

import server.commands.ICommand;

/**
 * A factory class that creates User Commands on demand.  Use this class to get a User Command
 * @author Danny Harding
 */
public class UserCommandFactory {

    private static UserCommandFactory instance = null;

    UserCommandFactory() {

    }

    public static UserCommandFactory getInstance() {
        if (instance == null) {
            instance = new UserCommandFactory();
        }

        return instance;
    }

    /**
     * Creates a user command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        return null;
    }
}
