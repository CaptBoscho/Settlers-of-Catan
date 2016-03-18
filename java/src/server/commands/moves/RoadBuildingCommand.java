package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.RoadBuildingException;
import server.facade.IFacade;
import shared.dto.IDTO;
import shared.dto.RoadBuildingDTO;

/**
 * A command object that plays a road building card
 *
 * @author Joel Bradley
 */
public class RoadBuildingCommand implements ICommand {

    private IFacade facade;
    private RoadBuildingDTO dto;

    /**
     * Constructor
     */
    public RoadBuildingCommand(IFacade facade, RoadBuildingDTO dto) {
        this.facade = facade;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Road Building command
     * @return IDTO
     */
    @Override
    public IDTO execute() {
        try {
            facade.roadBuilding(1, dto.getPlayerIndex(), dto.getRoadLocationOne(), dto.getRoadLocationTwo());
        } catch (RoadBuildingException e) {
            e.printStackTrace();
        }
        //TODO: change this later fool
        return null;
    }

}
