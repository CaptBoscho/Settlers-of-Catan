package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.commands.ICommand;
import server.commands.games.CreateCommand;
import server.commands.games.JoinCommand;
import server.commands.games.ListCommand;
import server.exceptions.CommandExecutionFailedException;
import shared.dto.IDTO;

import java.util.HashMap;
import java.util.Map;

import static server.commands.CommandName.*;
import static server.utils.Strings.BAD_COMMAND_NAME_MSG;

/**
 * A factory class that creates Games Commands on demand.  Use this class to get a Games Command
 *
 * @author Danny Harding, Derek Argueta
 * {@link} https://en.wikipedia.org/wiki/Command_pattern#Java_8
 */
public final class GamesCommandFactory {

    private final Map<CommandName, ICommand> commands;

    private static GamesCommandFactory instance = null;

    private GamesCommandFactory() {
        commands = new HashMap<>();
    }

    private void addCommand(final CommandName name, final ICommand command) {
        commands.put(name, command);
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

        throw new Exception(BAD_COMMAND_NAME_MSG);
    }

    public CommandExecutionResult executeCommand(final CommandName name) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                return command.execute();
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception(BAD_COMMAND_NAME_MSG);
    }

    public static GamesCommandFactory getInstance() {
        if(instance == null) {
            instance = new GamesCommandFactory();
            instance.addCommand(GAMES_LIST, new ListCommand());
            instance.addCommand(GAMES_JOIN, new JoinCommand());
            instance.addCommand(GAMES_CREATE, new CreateCommand());
        }

        return instance;
    }

}
