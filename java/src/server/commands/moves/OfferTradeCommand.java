package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.OfferTradeException;
import server.main.Config;
import shared.dto.CookieWrapperDTO;
import shared.dto.IDTO;
import shared.dto.OfferTradeDTO;

/**
 * A command object that offers a trade
 *
 * @author Joel Bradley
 */
public class OfferTradeCommand implements ICommand {

    private int gameId;
    private OfferTradeDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Offer Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.offerTrade(this.gameId, this.dto);
        }catch(OfferTradeException e){
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(final IDTO dto) {
        final CookieWrapperDTO cookieDTO = (CookieWrapperDTO)dto;
        this.dto = (OfferTradeDTO)cookieDTO.getDto();
        this.gameId = cookieDTO.getGameId();
    }

}
