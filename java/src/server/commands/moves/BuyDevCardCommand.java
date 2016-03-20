package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuyDevCardException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.BuyDevCardDTO;
import shared.dto.GameModelDTO;
import shared.dto.IDTO;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public class BuyDevCardCommand implements ICommand {
    private IFacade facade;
    private BuyDevCardDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        return null;
//            return facade.buyDevCard(1, dto.getPlayerIndex());
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
