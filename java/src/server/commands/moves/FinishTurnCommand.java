package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

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
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
