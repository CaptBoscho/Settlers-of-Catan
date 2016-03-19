package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

/**
 * A command object that plays a road building card
 *
 * @author Joel Bradley
 */
public class RoadBuildingCommand implements ICommand {

    /**
     * Constructor
     */
    public RoadBuildingCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Road Building command
     * @return JsonObject
     */
    @Override
    public GameModelDTO execute() {
        return null;
    }

}
