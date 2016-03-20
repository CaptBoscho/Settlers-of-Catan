package server.commands.moves;

import server.commands.ICommand;
import server.exceptions.AcceptTradeException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.GameModelDTO;
import shared.dto.TradeOfferResponseDTO;

/**
 * A command object that accepts a trade
 *
 * @author Joel Bradley
 */
public class AcceptTradeCommand implements ICommand {
    private IFacade facade;
    private int playerIndex;
    private boolean answer;
    /**
     * Constructor
     */
    public AcceptTradeCommand(TradeOfferResponseDTO dto, IFacade fac) {
        facade = fac;
        playerIndex = dto.getPlayerIndex();
        answer = dto.willAccept();
    }

    /**
     * Communicates with the ServerFacade to carry out the Accept Trade command
     * @return IDTO
     */
    @Override
    public GameModelDTO execute() throws CommandExecutionFailedException {
        try {
            return facade.acceptTrade(1, playerIndex, answer);
        } catch(AcceptTradeException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

}