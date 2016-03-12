package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.facade.IFacade;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public class BuildCityCommand implements ICommand {

    /**
     * Constructor
     */
    public BuildCityCommand() {

    }

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return JsonObject
     */
    @Override
    public JsonObject execute(IFacade facade) {
        return null;
    }

}
