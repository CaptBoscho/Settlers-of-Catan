package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
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
     * Communicates with the ServerFacade to carry out the Road Building command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return facade.roadBuilding(1, dto.getPlayerIndex(), dto.getRoadLocationOne(), dto.getRoadLocationTwo());
//        } catch (RoadBuildingException e) {
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
