package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RollNumberException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.RollNumberDTO;

/**
 * A command object that rolls a number
 *
 * @author Joel Bradley
 */
public class RollNumberCommand implements ICommand {

    private RollNumberDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Roll Number command
     * @return JsonObject
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.rollNumber(1, dto.getPlayerIndex(), dto.getValue());
        } catch (RollNumberException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (RollNumberDTO)dto;
    }
}
