package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuyDevCardException;
import server.exceptions.CommandExecutionFailedException;
import server.main.Config;
import shared.dto.BuyDevCardDTO;
import shared.dto.IDTO;

/**
 * A command object that buys a development card
 *
 * @author Joel Bradley
 */
public class BuyDevCardCommand implements ICommand {

    private BuyDevCardDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Buy Development Card command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.buyDevCard(1, dto.getPlayerIndex());
        } catch (BuyDevCardException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (BuyDevCardDTO)dto;
    }

}
