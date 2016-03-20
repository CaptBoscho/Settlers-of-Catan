package server.commands.moves;

import server.commands.CommandExecutionResult;
import server.commands.ICommand;
import server.exceptions.CommandExecutionFailedException;
import server.exceptions.SoldierException;
import server.main.Config;
import shared.dto.IDTO;
import shared.dto.PlaySoldierCardDTO;

/**
 * A command object that plays a soldier card
 *
 * @author Joel Bradley
 */
public class SoldierCommand implements ICommand {

    private PlaySoldierCardDTO dto;

    /**
     * Communicates with the ServerFacade to carry out the Soldier command
     * @return IDTO
     */
    @Override
    public CommandExecutionResult execute() throws CommandExecutionFailedException {
        try {
            return Config.facade.soldier(1, dto.getPlayerIndex(), dto.getLocation(), dto.getVictimIndex());
        } catch (SoldierException e) {
            throw new CommandExecutionFailedException(e.getMessage());
        }
    }

    @Override
    public void setParams(IDTO dto) {
        this.dto = (PlaySoldierCardDTO)dto;
    }

}
