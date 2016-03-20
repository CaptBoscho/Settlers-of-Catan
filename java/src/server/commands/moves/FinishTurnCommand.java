package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.FinishTurnException;
import server.main.Config;
import shared.dto.FinishTurnDTO;
import shared.dto.IDTO;

/**
 * A command object that finishes a turn
 *
 * @author Joel Bradley
 */
public class FinishTurnCommand implements ICommand {

    private FinishTurnDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Finish Turn command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.finishTurn(1, dto.getPlayerIndex());
        } catch (FinishTurnException e) {
            e.printStackTrace();
            throw new CommandExecutionFailedException("Error ending player turn!");
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (FinishTurnDTO)dto;
    }

}
