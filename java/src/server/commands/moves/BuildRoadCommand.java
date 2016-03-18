package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that builds a road
 *
 * @author Joel Bradley
 */
public class BuildRoadCommand implements ICommand {

    /**
     * Constructor
     */
    public BuildRoadCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Build Road command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
