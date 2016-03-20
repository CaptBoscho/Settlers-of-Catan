package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildCityException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.BuildCityDTO;
import shared.dto.IDTO;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public class BuildCityCommand implements ICommand {

    private IFacade facade;
    private BuildCityDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return facade.buildCity(1, dto.getPlayerIndex(), dto.getLocation());
//        } catch (BuildCityException e) {
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
