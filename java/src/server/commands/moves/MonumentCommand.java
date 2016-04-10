package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MonumentException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.PlayMonumentDTO;

import java.io.Serializable;

/**
 * A command object that plays a monument card
 *
 * @author Joel Bradley
 */
public final class MonumentCommand implements Serializable, ICommand {

    private boolean paramsSet = false;
    private int gameId;
    private int playerIndex;

    /**
     * Communicates with the ServerFacade to carry out the Monument command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        assert this.paramsSet;
        assert this.gameId >= 0;
        assert playerIndex >= 0;
        assert playerIndex < 4;

        try {
            return Config.facade.monument(this.gameId, this.playerIndex);
        } catch (MonumentException e) {
            throw new CommandExecutionFailedException("MonumentCommand failed to execute properly");
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        assert dto != null;

        this.paramsSet = true;
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        final PlayMonumentDTO tmpDTO = (PlayMonumentDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
        this.playerIndex = tmpDTO.getPlayerIndex();
    }
}