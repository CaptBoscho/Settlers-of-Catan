package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildCityException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildCityDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.VertexLocation;

/**
 * A command object that builds a city
 *
 * @author Joel Bradley
 */
public class BuildCityCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private VertexLocation location;

    /**
     * Communicates with the ServerFacade to carry out the Build City command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.buildCity(this.gameId, this.playerIndex, this.location);
        } catch (BuildCityException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuildCityDTO tmpDTO = (BuildCityDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getLocation();
    }

}
