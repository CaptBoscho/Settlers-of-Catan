package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

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
    public JsonObject execute() {
        return null;
    }

}
