package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.FinishTurnException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.FinishTurnDTO;
import shared.dto.IDTO;

import java.io.Serializable;

/**
 * A command object that finishes a turn
 *
 * @author Joel Bradley
 */
public final class FinishTurnCommand implements Serializable, ICommand {

    private int gameId;
    private int playerIndex;

    /**
     * Communicates with the ServerFacade to carry out the Finish Turn command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.finishTurn(this.gameId, this.playerIndex);
        } catch (FinishTurnException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Error ending player turn!");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final FinishTurnDTO tmpDTO = (FinishTurnDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
    }

}
