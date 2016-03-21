package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonumentException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlayMonumentDTO;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public class MonumentCommand implements ICommand {

    private int gameId;
    private int playerIndex;

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.monument(this.gameId, this.playerIndex);
        } catch (MonumentException e) {
            throw new CommandExecutionFailedException("MonumentCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlayMonumentDTO tmpDTO = (PlayMonumentDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
    }
}