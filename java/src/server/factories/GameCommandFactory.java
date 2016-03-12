package server.factories;

import server.commands.ICommand;
import server.commands.game.*;

/**
 * A factory class that creates Game Commands on demand. Use this class to get a Game Command.
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private static GameCommandFactory instance = null;

    private GameCommandFactory() {

    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
        }

        return instance;
    }

    /**
     * Creates a game command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        assert command != null;

        switch(command) {
            case "model":
                return new ModelCommand();
            case "addAI":
                return new AddAICommand();
            case "listAI":
                return new ListAICommand();
            default:
                return null;
        }
    }

}
