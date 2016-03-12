package server.commands.game;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that list the AI
 *
 * @author Joel Bradley
 */
public class ListAICommand implements ICommand {

    /**
     * Constructor
     */
    public ListAICommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the List AI command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
