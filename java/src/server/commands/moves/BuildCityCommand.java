package server.commands.moves;

import com.google.gson.JsonObject;
import server.commands.ICommand;
import server.exceptions.BuildCityException;
import server.facade.IFacade;
import shared.dto.BuildCityDTO;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public class BuildCityCommand implements ICommand {

    private IFacade facade;
    private BuildCityDTO dto;

    /**
     * Constructor
     */
    public BuildCityCommand(IFacade facade, BuildCityDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return JsonObject
     */
    @Override
    public JsonObject execute() {
        try {
            facade.buildCity(dto.getPlayerIndex(), dto.getLocation());
        } catch (BuildCityException e) {
            e.printStackTrace();
        }
        //TODO: change this later fool
        return null;
    }

}
