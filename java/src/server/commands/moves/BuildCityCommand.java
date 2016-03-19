package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import shared.dto.GameModelDTO;

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
    public GameModelDTO execute() {
        return null;
    }

}
