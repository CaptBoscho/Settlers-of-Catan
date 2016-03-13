package server.factories;

import server.commands.ICommand;
import server.commands.user.*;
import server.facade.IFacade;
import server.facade.ServerFacade;

/**
 * A factory class that creates User Commands on demand.  Use this class to get a User Command
 * @author Danny Harding
 */
public class UserCommandFactory {

    private IFacade facade;
    private static UserCommandFactory instance = null;

    private UserCommandFactory() {
        facade = new ServerFacade();
    }

    public static UserCommandFactory getInstance() {
        if (instance == null) {
            instance = new UserCommandFactory();
        }

        return instance;
    }

    public void bind(IFacade new_facade){
        facade = new_facade;
    }

    /**
     * Creates a user command based on a given string
     * @param command The string indicating what type of command to return
     * @return an ICommand object
     */
    public ICommand createCommand(String command) {
        assert command != null;

        switch(command) {
            case "login":
                return new LoginCommand();
            case "register":
                return new RegisterCommand();
            default:
                return null;
        }
    }
}
