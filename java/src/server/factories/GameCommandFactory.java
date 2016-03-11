package server.factories;

import server.commands.ICommand;

/**
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private static GameCommandFactory instance = null;

    GameCommandFactory() {

    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
        }

        return instance;
    }

    public ICommand createCommand(String command) {
        return null;
    }
}
