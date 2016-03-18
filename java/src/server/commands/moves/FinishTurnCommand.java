package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

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
    public GameModelDTO execute() {
        return null;
    }

}
