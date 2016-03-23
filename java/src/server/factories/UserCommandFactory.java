package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.commands.ICommand;
import server.commands.user.LoginCommand;
import server.commands.user.RegisterCommand;
import server.exceptions.CommandExecutionFailedException;
import shared.dto.IDTO;

import static server.commands.CommandName.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class that creates User Commands on demand.  Use this class to get a User Command
 * @author Danny Harding
 */
public final class UserCommandFactory {

    private final Map<CommandName, ICommand> commands;
    private static UserCommandFactory instance = null;

    private void addCommand(final CommandName name, final ICommand command) {
        commands.put(name, command);
    }

    private UserCommandFactory() {
        commands = new HashMap<>();
    }

    public CommandExecutionResult executeCommand(final CommandName name, final IDTO dto) throws Exception {
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
            instance.addCommand(USER_LOGIN, new LoginCommand());
            instance.addCommand(USER_REGISTER, new RegisterCommand());
        }

        return instance;
    }

}
