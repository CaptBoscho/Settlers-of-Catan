package server.factories;

import server.commands.ICommand;
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
        facade = ServerFacade.getInstance();
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
     * @return an ICommand object
     */
    public ICommand createCommand(Object obj) {
        //TODO: use AuthDTO to create
        return null;
    }
}
