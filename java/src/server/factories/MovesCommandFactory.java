package server.factories;

import server.commands.ICommand;

/**
 * A factory class that creates Moves Commands on demand.  Use this class to get a Moves Command
 */
public class MovesCommandFactory {

    private static MovesCommandFactory instance = null;

    MovesCommandFactory() {

    }

    public static MovesCommandFactory getInstance() {
        if (instance == null) {
            instance = new MovesCommandFactory();
        }

        return instance;
    }

    /**
     * Creates a Moves command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        return null;
    }
}
