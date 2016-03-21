package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.commands.user.LoginCommand;
import server.commands.user.RegisterCommand;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.IDTO;

import java.util.HashMap;

/**
 * A factory class that creates User Commands on demand.  Use this class to get a User Command
 * @author Danny Harding
 */
public class UserCommandFactory {

    private IFacade facade;
    private final HashMap<String, ICommand> commands;
    private static UserCommandFactory instance = null;

    private void addCommand(final String name, final ICommand command) {
        commands.put(name, command);
    }

    private UserCommandFactory() {
        facade = ServerFacade.getInstance();
        commands = new HashMap<>();
    }

    public CommandExecutionResult executeCommand(final String name, final IDTO dto) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                command.setParams(dto);
                // TODO - break out into "execute" and "fetchResult"
                return command.execute();
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception("no matching command found");
    }

    public static UserCommandFactory getInstance() {
        if (instance == null) {
            instance = new UserCommandFactory();
            instance.addCommand("login", new LoginCommand());
            instance.addCommand("register", new RegisterCommand());
        }

        return instance;
    }

}
