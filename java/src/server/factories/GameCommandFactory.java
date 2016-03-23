package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.CommandName;
import server.commands.ICommand;
import server.commands.game.AddAICommand;
import server.commands.game.ListAICommand;
import server.commands.game.ModelCommand;
import server.exceptions.CommandExecutionFailedException;
import shared.dto.IDTO;

import java.util.HashMap;
import java.util.Map;

import static server.commands.CommandName.*;
import static server.utils.Strings.BAD_COMMAND_NAME_MSG;


/**
 * A factory class that creates Game Commands on demand. Use this class to get a Game Command.
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private static GameCommandFactory instance = null;
    private Map<CommandName, ICommand> commands;

    private GameCommandFactory() {
        this.commands = new HashMap<>();
    }

    private void addCommand(final CommandName name, final ICommand command) {
        this.commands.put(name, command);
    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
            instance.addCommand(GAME_MODEL, new ModelCommand());
            instance.addCommand(GAME_LIST_AI, new ListAICommand());
            instance.addCommand(GAME_ADD_AI, new AddAICommand());
        }

        return instance;
    }

    public CommandExecutionResult executeCommand(final CommandName name, final IDTO dto) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                command.setParams(dto);
                // TODO - break out into "execute" and "fetchResult"
                return commands.get(name).execute();
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception(BAD_COMMAND_NAME_MSG);
    }
}
