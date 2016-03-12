package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public class MaritimeTradeCommand implements ICommand {

    /**
     * Constructor
     */
    public MaritimeTradeCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
