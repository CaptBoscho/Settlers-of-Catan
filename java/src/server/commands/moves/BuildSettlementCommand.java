package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildSettlementException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuildSettlementDTO;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.locations.VertexLocation;

/**
 * A command object that builds a settlement
 *
 * @author Joel Bradley
 */
public final class BuildSettlementCommand implements ICommand {

    private int gameId;
    private int playerIndex;
    private VertexLocation location;

    /**
     * Communicates with the ServerFacade to carry out the Build Settlement command
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.gameId >= 0;
        assert this.playerIndex >= 0;
        assert this.playerIndex < 4;
        assert location != null;

        try {
            return Config.facade.buildSettlement(this.gameId, this.playerIndex, this.location);
        } catch (BuildSettlementException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final BuildSettlementDTO tmpDTO = (BuildSettlementDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
        this.location = tmpDTO.getLocation();
    }

}
