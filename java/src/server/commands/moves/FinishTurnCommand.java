package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

/**
 * A command object that finishes a turn
 *
 * @author Joel Bradley
 */
public class FinishTurnCommand implements ICommand {

    /**
     * Constructor
     */
    public FinishTurnCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Finish Turn command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        return null;
    }

}
