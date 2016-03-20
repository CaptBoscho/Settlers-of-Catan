package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.commands.games.CreateCommand;
import server.commands.games.JoinCommand;
import server.commands.games.ListCommand;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import server.facade.ServerFacade;
import shared.dto.IDTO;

import java.util.HashMap;

/**
 * A factory class that creates Games Commands on demand.  Use this class to get a Games Command
 *
 * @author Danny Harding, Derek Argueta
 * {@link} https://en.wikipedia.org/wiki/Command_pattern#Java_8
 */
public class GamesCommandFactory {

    private final HashMap<String, ICommand> commands;

    private IFacade facade;
    private static GamesCommandFactory instance = null;

    private GamesCommandFactory() {
        facade = ServerFacade.getInstance();
        commands = new HashMap<>();
    }

    private void addCommand(final String name, final ICommand command) {
        commands.put(name, command);
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

    public CommandExecutionResult executeCommand(final String name) throws Exception {
        if(commands.containsKey(name)) {
            try {
                ICommand command = commands.get(name);
                return command.execute();
            } catch (CommandExecutionFailedException e) {
                e.printStackTrace();
            }
        }

        throw new Exception("no matching command found");
    }

    public static GamesCommandFactory getInstance() {
        if(instance == null) {
            instance = new GamesCommandFactory();
            instance.addCommand("list", new ListCommand());
            instance.addCommand("join", new JoinCommand());
            instance.addCommand("create", new CreateCommand());
        }

        return instance;
    }

    public void bind(IFacade newFacade) {
        facade = newFacade;
    }
}
