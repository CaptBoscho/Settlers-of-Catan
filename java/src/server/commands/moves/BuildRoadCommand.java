package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;

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
    public JsonObject execute() {
        return null;
    }

}
