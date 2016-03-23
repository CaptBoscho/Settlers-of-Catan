package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.DiscardCardsException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.DiscardCardsDTO;
import shared.dto.IDTO;

/**
 * A command object that discards cards
 *
 * @author Joel Bradley
 */
public final class DiscardCardsCommand implements ICommand {

    private int gameId;
    private DiscardCardsDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Discard Cards command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.discardCards(gameId, dto);
        }catch(DiscardCardsException e){
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        this.dto = (DiscardCardsDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }
}
