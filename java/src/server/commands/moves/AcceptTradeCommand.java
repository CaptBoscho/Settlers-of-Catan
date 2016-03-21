package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.AcceptTradeException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.TradeOfferResponseDTO;

/**
 * A command object that accepts a trade
 *
 * @author Joel Bradley
 */
public class AcceptTradeCommand implements ICommand {

    private TradeOfferResponseDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Accept Trade command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.acceptTrade(1, dto.getPlayerIndex(), dto.willAccept());
        } catch(AcceptTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (TradeOfferResponseDTO)dto;
    }

}