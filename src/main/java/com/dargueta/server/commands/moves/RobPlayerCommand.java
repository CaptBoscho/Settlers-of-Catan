package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.RobPlayerException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.RobPlayerDTO;

/**
 * A command object that robs a player
 *
 * @author Joel Bradley
 */
public class RobPlayerCommand implements ICommand {

    private RobPlayerDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Rob Player command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.robPlayer(1, dto.getPlayerIndex(), dto.getLocation(), dto.getVictimIndex());
        } catch (RobPlayerException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (RobPlayerDTO)dto;
    }

}
