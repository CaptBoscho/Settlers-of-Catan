package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.BuildRoadException;
import server.facade.IFacade;
import shared.dto.BuildRoadDTO;
import shared.dto.GameModelDTO;

/**
 * A command object that builds a road
 *
 * @author Joel Bradley
 */
public class BuildRoadCommand implements ICommand {

    private IFacade facade;
    private BuildRoadDTO dto;

    /**
     * Constructor
     */
    public BuildRoadCommand(IFacade facade, BuildRoadDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Build Road command
     * @return GameModelDTO
     */
    @Override
    public GameModelDTO execute() {
        try {
            facade.buildRoad(0, dto.getPlayerIndex(), dto.getRoadLocation());
        } catch (BuildRoadException e) {
            e.printStackTrace();
        }
        //TODO: change this later fool
        return null;
    }

}
