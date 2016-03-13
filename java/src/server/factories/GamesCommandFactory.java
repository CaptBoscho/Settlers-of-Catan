package server.factories;

import server.commands.ICommand;
import server.commands.games.*;
import server.facade.IFacade;
import server.facade.ServerFacade;

/**
 * A factory class that creates Games Commands on demand.  Use this class to get a Games Command
 * Created by Danny Harding on 3/10/16.
 */
public class GamesCommandFactory {

    private IFacade facade;
    private static GamesCommandFactory instance = null;

    private GamesCommandFactory() {
        facade = new ServerFacade();
    }

    public static GamesCommandFactory getInstance() {
        if (instance == null) {
            instance = new GamesCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }

    /**
     * Creates a Games command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        assert command != null;

        switch(command) {
            case "list":
                return new ListCommand();
            case "create":
                return new CreateCommand();
            case "join":
                return new JoinCommand();
            default:
                return null;
        }
    }
}
