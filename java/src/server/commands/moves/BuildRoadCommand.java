package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.BuildRoadException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.BuildRoadDTO;
import shared.dto.IDTO;

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
     * @return IDTO
     */
    @Override
    public IDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.buildRoad(1, dto.getPlayerIndex(), dto.getRoadLocation());
        } catch (BuildRoadException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

}
