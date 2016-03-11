package server.factories;

import server.commands.ICommand;
import server.commands.game.*;

/**
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
