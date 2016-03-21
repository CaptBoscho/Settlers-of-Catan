package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.MaritimeTradeException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.MaritimeTradeDTO;

/**
 * A command object that maritime trades
 *
 * @author Joel Bradley
 */
public class MaritimeTradeCommand implements ICommand {

    private int gameId;
    private MaritimeTradeDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Maritime Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.maritimeTrade(1, dto);
        } catch(MaritimeTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO) dto;
        this.dto = (MaritimeTradeDTO) cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }
}
