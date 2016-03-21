package server.factories;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.commands.game.AddAICommand;
import server.commands.game.ListAICommand;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.IDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory class that creates Game Commands on demand. Use this class to get a Game Command.
 * @author Derek Argueta
 */
public class GameCommandFactory {

    private IFacade facade;
    private static GameCommandFactory instance = null;
    private Map<String, ICommand> commands;

    private GameCommandFactory() {
//        this.facade = ServerFacade.getInstance();
        this.commands = new HashMap<>();
    }

    private void addCommand(final String name, final ICommand command) {
        this.commands.put(name, command);
    }

    public static GameCommandFactory getInstance() {
        if(instance == null) {
            instance = new GameCommandFactory();
//            instance.addCommand("getModel", );
            instance.addCommand("listAI", new ListAICommand());
            instance.addCommand("addAI", new AddAICommand());
        }

        return instance;
    }

    public CommandExecutionResult executeCommand(final String name, final IDTO dto) throws Exception {
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

        throw new Exception("no matching command found");
    }


}
