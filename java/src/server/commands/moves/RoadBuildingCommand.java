package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

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
    public JsonObject execute() {
        return null;
    }

}
