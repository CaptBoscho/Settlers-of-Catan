package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RoadBuildingException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.RoadBuildingDTO;
import shared.locations.EdgeLocation;

/**
 * A command object that plays a road building card
 *
 * @author Joel Bradley
 */
public class RoadBuildingCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private EdgeLocation locationOne;
    private EdgeLocation locationTwo;

    /**
     * Communicates with the ServerFacade to carry out the Road Building command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.roadBuilding(this.gameId, this.playerIndex, this.locationOne, this.locationTwo);
        } catch (RoadBuildingException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final RoadBuildingDTO tmpDTO = (RoadBuildingDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.locationOne = tmpDTO.getRoadLocationOne();
        this.locationTwo = tmpDTO.getRoadLocationTwo();
    }

}
