package server.factories;

import server.commands.ICommand;
import server.commands.game.AddAICommand;
import server.commands.game.ListAICommand;
import server.facade.IFacade;

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

    public void bind(IFacade newFacade){
        facade = newFacade;
    }

}
