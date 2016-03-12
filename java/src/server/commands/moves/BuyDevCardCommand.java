package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public class BuyDevCardCommand implements ICommand {

    /**
     * Constructor
     */
    public BuyDevCardCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
