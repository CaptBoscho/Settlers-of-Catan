package server.commands.moves;

import client.services.CommandExecutionFailed;
import server.commands.ICommand;
import server.exceptions.AcceptTradeException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.TradeOfferResponseDTO;
import shared.exceptions.PlayerExistsException;
import shared.model.bank.InvalidTypeException;

import javax.naming.InsufficientResourcesException;
import shared.dto.GameModelDTO;

/**
 * A command object that accepts a trade
 *
 * @author Joel Bradley
 */
public class AcceptTradeCommand implements ICommand {
    private IFacade facade;
    private TradeOfferResponseDTO dto;
    /**
     * Constructor
     */
    public AcceptTradeCommand(TradeOfferResponseDTO dto, IFacade fac) {
        facade = fac;
        this.dto = dto;
    }

    /**
     * Communicates with the ServerFacade to carry out the Accept Trade command
     * @return IDTO
     */
    @Override
    public GameModelDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.acceptTrade(1, dto);
        } catch(AcceptTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

}