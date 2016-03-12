package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that plays a monopoly card
 *
 * @author Joel Bradley
 */
public class MonopolyCommand implements ICommand {

    /**
     * Constructor
     */
    public MonopolyCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Monopoly command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
