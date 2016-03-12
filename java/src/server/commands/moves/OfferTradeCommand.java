package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that offers a trade
 *
 * @author Joel Bradley
 */
public class OfferTradeCommand implements ICommand {

    /**
     * Constructor
     */
    public OfferTradeCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        return null;
    }

}
