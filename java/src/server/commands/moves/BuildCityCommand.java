package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildCityException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public class BuildCityCommand implements ICommand {

    private CookieWrapperDTO cookies;
    private BuildCityDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.buildCity(cookies.getGameId(), dto.getPlayerIndex(), dto.getLocation());
        } catch (BuildCityException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.cookies = (CookieWrapperDTO)dto;
        this.dto = (BuildCityDTO)cookies.getDto();
    }

}
