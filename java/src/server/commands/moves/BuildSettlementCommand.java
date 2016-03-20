package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.BuildSettlementException;
import server.exceptions.CommandExecutionFailedException;
import server.facade.IFacade;
import shared.dto.BuildSettlementDTO;
import shared.dto.IDTO;

/**
 * A command object that builds a settlement
 *
 * @author Joel Bradley
 */
public class BuildSettlementCommand implements ICommand {

    private IFacade facade;
    private BuildSettlementDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Build Settlement command
     * @return CommandExecutionResult
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
//        try {
//            return facade.buildSettlement(1, dto.getPlayerIndex(), dto.getLocation());
//        } catch (BuildSettlementException e) {
//            throw new CommandExecutionFailedException(e.getMessage());
//        }
        return null;
    }

    @Override
    public void setParams(IDTO dto) {

    }

}
