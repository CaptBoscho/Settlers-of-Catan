package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildRoadException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildRoadDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.EdgeLocation;

/**
 * A command object that builds a road
 *
 * @author Joel Bradley
 */
public final class BuildRoadCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private EdgeLocation location;

    /**
     * Communicates with the ServerFacade to carry out the Build Road command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.buildRoad(this.gameId, this.playerIndex, this.location);
        } catch (BuildRoadException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuildRoadDTO tmpDTO = (BuildRoadDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getRoadLocation();
    }

}
