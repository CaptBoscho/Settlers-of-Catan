package server.factories;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.commands.game.*;
import server.facade.IFacade;
import server.facade.ServerFacade;

/**
 * A factory class that creates Game Commands on demand. Use this class to get a Game Command.
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private IFacade facade;
    private static GameCommandFactory instance = null;

    private GameCommandFactory() {
        facade = new ServerFacade();
    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }


    /**
     * Creates a game command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command, JsonObject object) {
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
