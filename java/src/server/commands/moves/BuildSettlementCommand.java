package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that builds a settlement
 *
 * @author Joel Bradley
 */
public class BuildSettlementCommand implements ICommand {

    /**
     * Constructor
     */
    public BuildSettlementCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Build Settlement command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        return null;
    }

}
